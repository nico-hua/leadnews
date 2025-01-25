package com.heima.article.service.impl;

import com.heima.article.service.ApArticleBehaviorDetailService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.behavior.vos.ArticleInfoVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApArticleBehaviorDetailServiceImpl implements ApArticleBehaviorDetailService {
    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult behaviorDetails(ArticleInfoDto dto) {
        // 1.登录校验
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        // 2.参数校验
        if (dto == null||dto.getArticleId()==null||dto.getAuthorId()==null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询点赞、收藏、不喜欢记录,是否关注该作者
        String likeKey = BehaviorConstants.LIKE_ARTICLE + dto.getArticleId();
        String unlikeKey = BehaviorConstants.UNLIKE_ARTICLE + dto.getArticleId();
        String collectKey = BehaviorConstants.COLLECTION_ARTICLE + dto.getArticleId();
        String followKey = BehaviorConstants.FOLLOW_AUTHOR + dto.getAuthorId();
        boolean islike = cacheService.hGet(likeKey, userId) != null;
        boolean isunlike = cacheService.hGet(unlikeKey, userId) != null;
        boolean iscollect = cacheService.hGet(collectKey, userId) != null;
        boolean isfollow = cacheService.hGet(followKey, userId) != null;
        return ResponseResult.okResult(new ArticleInfoVo(islike,isunlike,iscollect,isfollow));
    }
}
