package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private CacheService cacheService;
    @Override
    public void computeHotArticle() {
        // 查询前5天的文章
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);
        // 计算文章的分值
        List<HotArticleVo> hotArticleListVoList = computeHotArticle(apArticleList);
        // 为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleListVoList);
    }

    /**
     * 为每个频道缓存30条分值较高的文章
     * */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleListVoList){
        ResponseResult responseResult = wemediaClient.getChannels();
        if(responseResult.getCode()==200){
            String channelJson = JSON.toJSONString(responseResult.getData());
            List<WmChannel> wmChannels = JSON.parseArray(channelJson, WmChannel.class);
            if(wmChannels!=null&&wmChannels.size()>0){
                for(WmChannel wmChannel : wmChannels){
                    List<HotArticleVo> hotArticleVos = hotArticleListVoList.stream().filter(x->
                            x.getChannelId().equals(wmChannel.getId())
                    ).collect(Collectors.toList());
                    sortAndCache(hotArticleVos, ArticleConstants.HOT_ARTICLE_FIRST_PAGE+wmChannel.getId());
                }
            }
        }
        sortAndCache(hotArticleListVoList,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 排序并缓存
     * */
    private void sortAndCache(List<HotArticleVo> hotArticleListVoList,String key){
        hotArticleListVoList = hotArticleListVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore)).collect(Collectors.toList());
        if(hotArticleListVoList.size()>30){
            hotArticleListVoList = hotArticleListVoList.subList(0,30);
        }
        cacheService.set(key,JSON.toJSONString(hotArticleListVoList));
    }

    /**
     * 计算文章分值
     * */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList){
        List<HotArticleVo> hotArticleList = new ArrayList<>();
        if(apArticleList!=null&&apArticleList.size()>0){
            for (ApArticle apArticle : apArticleList) {
                HotArticleVo hotArticleVo = new HotArticleVo();
                BeanUtils.copyProperties(apArticle,hotArticleVo);
                Integer score = computeScore(apArticle);
                hotArticleVo.setScore(score);
                hotArticleList.add(hotArticleVo);
            }
        }
        return hotArticleList;
    }

    /**
     * 计算文章的具体分值
     * */
    private Integer computeScore(ApArticle apArticle){
        Integer score = 0;
        if(apArticle.getLikes()!=null){
            score += apArticle.getLikes()* ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(apArticle.getViews()!=null){
            score += apArticle.getViews();
        }
        if(apArticle.getComment()!=null){
            score += apArticle.getComment()* ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(apArticle.getCollection()!=null){
            score += apArticle.getCollection()* ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return score;
    }
}



























