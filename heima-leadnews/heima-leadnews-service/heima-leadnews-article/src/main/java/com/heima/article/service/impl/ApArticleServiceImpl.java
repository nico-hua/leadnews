package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.model.wemedia.dtos.NewsDimensionDto;
import com.heima.model.wemedia.dtos.NewsPageDto;
import com.heima.model.wemedia.vos.NewsCommentsVo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    // 单页最大加载数量
    private final static Integer MAX_PAGE_SIZE = 50;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;

    @Autowired
    private CacheService cacheService;
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 1. 校验参数
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        size = Math.min(size, MAX_PAGE_SIZE);
        dto.setSize(size);
        // 2. 类型参数校验
        if(!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE)&&!type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        // 3. 文章频道校验
        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // 4. 时间校验
        if(dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }
        if(dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
        // 5. 执行查询
        List<ApArticle> list = apArticleMapper.loadArticleList(dto, type);
        // 6. 结果返回
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        // 1. 校验参数
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);
        // 2. 判断id是否存在
        // 2.1 不存在，保存文章、文章配置、文章内容
        if(dto.getId() == null){
            // 保存文章
            save(apArticle);
            // 保存文章配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);
            // 保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);
        }
        // 2.2 存在，修改文章、文章内容
        else{
            // 修改文章
            updateById(apArticle);
            // 修改文章内容
            LambdaQueryWrapper<ApArticleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ApArticleContent::getArticleId,dto.getId());
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(lambdaQueryWrapper);
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }
        // 异步调用 生成静态文件上传到minio
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    articleFreemarkerService.buildArticleToMinIO(apArticle,dto.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // 返回文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if(firstPage){
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if(StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVoList = JSON.parseArray(jsonStr, HotArticleVo.class);
                return ResponseResult.okResult(hotArticleVoList);
            }
        }
        return load(dto,type);
    }

    @Override
    public PageResponseResult findNewsComments(NewsCommentsDto dto) {
        dto.checkParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            if (!StringUtils.isEmpty(dto.getBeginDate())) {
                // 设置 beginDate 为零点
                beginDate = sdf.parse(dto.getBeginDate());
            }
            if (!StringUtils.isEmpty(dto.getEndDate())) {
                // 设置 endDate 为 23:59:59
                Date tempEndDate = sdf.parse(dto.getEndDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempEndDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        if(beginDate!=null){
            queryWrapper.ge(ApArticle::getCreatedTime,beginDate);
        }
        if(endDate!=null){
            queryWrapper.le(ApArticle::getCreatedTime,endDate);
        }
        queryWrapper.orderByDesc(ApArticle::getCreatedTime);
        IPage page = new Page(dto.getPage(),dto.getSize());
        page = page(page, queryWrapper);
        List<ApArticle> apArticleList = page.getRecords();
        List<NewsCommentsVo> newsCommentsVoList = new ArrayList<>();
        for(ApArticle apArticle:apArticleList){
            NewsCommentsVo newsCommentsVo = new NewsCommentsVo();
            BeanUtils.copyProperties(apArticle,newsCommentsVo);
            newsCommentsVo.setComments(apArticle.getComment());
            ApArticleConfig apArticleConfig = apArticleConfigMapper.selectOne(new LambdaQueryWrapper<ApArticleConfig>().eq(ApArticleConfig::getArticleId, apArticle.getId()));
            if(apArticleConfig!=null){
                newsCommentsVo.setIsComment(apArticleConfig.getIsComment());
                newsCommentsVo.setIsForward(apArticleConfig.getIsForward());
                newsCommentsVo.setIsDown(apArticleConfig.getIsDown());
                newsCommentsVo.setIsDelete(apArticleConfig.getIsDelete());
            }
            newsCommentsVoList.add(newsCommentsVo);
        }
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(newsCommentsVoList);
        return responseResult;
    }

    @Override
    public ResponseResult getNewsDimension(NewsDimensionDto dto) {
        Date beginDate =dto.getBeginDate();
        Date endDate = dto.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        endDate = calendar.getTime();
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApArticle::getAuthorId, dto.getWmUserId());
        queryWrapper.ge(ApArticle::getCreatedTime,beginDate);
        queryWrapper.le(ApArticle::getCreatedTime,endDate);
        List<ApArticle> apArticleList = apArticleMapper.selectList(queryWrapper);
        Integer publishNum = apArticleList.size();
        Integer likesNum = 0;
        Integer collectNum = 0;
        if(apArticleList.size()>0){
            for (ApArticle apArticle : apArticleList) {
                likesNum += apArticle.getLikes();
                collectNum += apArticle.getCollection();
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("publishNum", publishNum);
        map.put("likesNum", likesNum);
        map.put("collectNum", collectNum);
        return ResponseResult.okResult(map);
    }

    @Override
    public PageResponseResult getNewsPage(NewsPageDto dto) {
        Date beginDate =dto.getBeginDate();
        Date endDate = dto.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        endDate = calendar.getTime();
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApArticle::getAuthorId, dto.getWmUserId());
        queryWrapper.ge(ApArticle::getCreatedTime,beginDate);
        queryWrapper.le(ApArticle::getCreatedTime,endDate);
        IPage page = new Page(dto.getPage(),dto.getSize());
        page = page(page, queryWrapper);
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}























