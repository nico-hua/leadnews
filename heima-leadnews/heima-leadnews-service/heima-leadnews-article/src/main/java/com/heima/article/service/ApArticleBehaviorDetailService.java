package com.heima.article.service;

import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleBehaviorDetailService {
    /**
     * 返回用户对文章做了什么行为的详细记录
     */
    ResponseResult behaviorDetails(ArticleInfoDto info);
}
