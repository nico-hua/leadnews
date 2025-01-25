package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.model.wemedia.dtos.NewsDimensionDto;
import com.heima.model.wemedia.dtos.NewsPageDto;
import com.heima.model.wemedia.vos.NewsCommentsVo;

import java.util.Map;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 根据参数加载文章列表
     * @param dto
     * @param type 1 加载更多  2 加载最新
     * */
    ResponseResult load(ArticleHomeDto dto, Short type);

    /**
     * 保存app端相关文章
     * @param dto
     * @return
     */
    ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章列表
     * */
    ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage);

    /**
     * 加载文章及相关评论
     * */
    PageResponseResult findNewsComments(NewsCommentsDto dto);

    /**
     * 获取文章统计
     * */
    ResponseResult getNewsDimension(NewsDimensionDto dto);

    /**
     * 获取文章
     * */
    PageResponseResult getNewsPage(NewsPageDto dto);
}












