package com.heima.apis.article;

import com.heima.apis.article.fallback.IArticleClientFallback;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.model.wemedia.dtos.NewsDimensionDto;
import com.heima.model.wemedia.dtos.NewsPageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="leadnews-article", fallback = IArticleClientFallback.class)
@Primary
public interface IArticleClient {
    @PostMapping("/api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);

    @PostMapping("/api/v1/article/find_news_comments")
    PageResponseResult findNewsComments(@RequestBody NewsCommentsDto dto);

    @PostMapping("/api/v1/article/statistics/newsDimension")
    ResponseResult getNewsDimension(@RequestBody NewsDimensionDto dto);

    @PostMapping("/api/v1/statistics/newsPage")
    PageResponseResult getNewsPage(@RequestBody NewsPageDto dto);
}
