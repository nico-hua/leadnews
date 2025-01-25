package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.tess4j.Tess4jClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    @Autowired
    private Tess4jClient tess4jClient;

    @Override
    @Async
    public void autoScanWmNews(Integer id) {
        // 1. 查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if(wmNews==null){
            throw new RuntimeException("文章不存在");
        }
        if(wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            // 从内容中提取文本和图片
            Map<String,Object> textAndImages = handleTextAndImages(wmNews);
            // 自管理敏感词审核
            boolean isSensitiveScan = handleSensitiveScan((String) textAndImages.get("content"), wmNews);
            if(!isSensitiveScan){
                return;
            }
            // 文本审核
            boolean isTextScan = handleTextScan((String) textAndImages.get("content"), wmNews);
            if(!isTextScan){
                return;
            }
            // 图片审核
            boolean isImageScan = handleImageScan((List<String>) textAndImages.get("images"), wmNews);
            if(!isImageScan){
                return;
            }
            // 文章审核通过,保存app端文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);
            if(responseResult.getCode()!=200){
                System.out.println(responseResult.getCode()+":"+responseResult.getErrorMessage());
                throw new RuntimeException("保存app端文章数据失败");
            }
            wmNews.setArticleId((Long) responseResult.getData());
            wmNews.setStatus(WemediaConstants.WM_NEWS_STATUS_PUBLISHED);
            wmNews.setReason("审核成功");
            wmNewsMapper.updateById(wmNews);
        }
    }

    /*
     * 自管理的敏感词审核
     * */
    private boolean handleSensitiveScan(String content, WmNews wmNews){
        boolean flag = true;
        if(StringUtils.isBlank(content)){
            return true;
        }
        // 获取敏感词
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmSensitive::getStatus,1);
        lambdaQueryWrapper.select(WmSensitive::getSensitives);
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(lambdaQueryWrapper);
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
        // 初始化敏慢词库
        SensitiveWordUtil.initMap(sensitiveList);
        // 查看文章中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size()>0){
            flag = false;
            wmNews.setStatus((short) 2);
            wmNews.setReason("当前文章包含敏感词"+map);
            wmNewsMapper.updateById(wmNews);
        }
        return flag;
    }

    /*
      保存app端相关文章数据
      */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(wmNews,articleDto);
        articleDto.setLayout(wmNews.getType());
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel!=null){
            articleDto.setChannelName(wmChannel.getName());
        }
        articleDto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser!=null){
            articleDto.setAuthorName(wmUser.getName());
        }
        if(wmNews.getArticleId()!=null){
            articleDto.setId(wmNews.getArticleId());
        }
        articleDto.setCreatedTime(new Date());
        return articleClient.saveArticle(articleDto);
    }

    /**
     * 审核图片
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews){
        boolean flag = true;
        if(images==null||images.size()==0){
            return true;
        }
        // 图片去重
        images = images.stream().distinct().collect(Collectors.toList());
        try{
            boolean block = false;
            String blockReason = "";
            boolean review = false;
            String reviewReason = "";
            for(String image: images){
                byte[] imageBytes = fileStorageService.downLoadFile(image);
                // 图片文字审核
                ByteArrayInputStream in =new ByteArrayInputStream(imageBytes);
                BufferedImage imageFile = ImageIO.read(in);
                String result = tess4jClient.doOCR(imageFile);
                // System.out.println("=============="+result);
                boolean isSensitive = handleSensitiveScan(result, wmNews);
                if(!isSensitive){
                    return false;
                }
                // 阿里云图片审核
                Map map = greenImageScan.greenImageScan(image, imageBytes);
                if(map!=null){
                    if(map.get("suggestion").equals("block")){
                        block = true;
                        blockReason = (String) map.get("reason");
                        break;
                    }
                    if(map.get("suggestion").equals("review")){
                        review = true;
                        reviewReason = (String) map.get("reason");
                    }
                }
            }
            if(block){
                flag = false;
                wmNews.setStatus((short)2);
                wmNews.setReason(blockReason);
                wmNewsMapper.updateById(wmNews);
            }
            if(!block&&review){
                flag = false;
                wmNews.setStatus((short)3);
                wmNews.setReason(reviewReason);
                wmNewsMapper.updateById(wmNews);
            }
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /*
    * 审核文本
    * */
    private boolean handleTextScan(String content, WmNews wmNews){
        boolean flag = true;
        if(wmNews.getTitle().length()==0&&content.length()==0){
            return true;
        }
        try{
            Map map = greenTextScan.greeTextScan(wmNews.getTitle()+"-"+content);
            if(map!=null){
                // 审核不通过
                if(map.get("suggestion").equals("block")){
                    flag = false;
                    wmNews.setStatus((short)2);
                    wmNews.setReason((String) map.get("reason"));
                    wmNewsMapper.updateById(wmNews);
                }
                // 需要人工审核
                if(map.get("suggestion").equals("review")){
                    flag = false;
                    wmNews.setStatus((short)3);
                    wmNews.setReason((String) map.get("reason"));
                    wmNewsMapper.updateById(wmNews);
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /*
      1. 从自媒体文章中提取文本和图片
      2. 提取文章的封面图片
      */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> images = new ArrayList<>();
        // 提取文章标题
        if(StringUtils.isNotBlank(wmNews.getTitle())){
            stringBuilder.append(wmNews.getTitle()).append("-");
        }
        // 从自媒体文章的内容中提取文本和图片
        if(StringUtils.isNoneBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for(Map map : maps){
                if(map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
                if(map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        // 提取文章的封面图片
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }
        Map<String, Object> res = new HashMap<>();
        res.put("content",stringBuilder.toString());
        res.put("images",images);
        return res;
    }
}


















