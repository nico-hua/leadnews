package com.heima.article.feign;

import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.model.wemedia.dtos.NewsDimensionDto;
import com.heima.model.wemedia.dtos.NewsPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleClient implements IArticleClient {
    @Autowired
    private ApArticleService apArticleService;
    @PostMapping("/api/v1/article/save")
    @Override
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }

    @Override
    @PostMapping("api/v1/article/find_news_comments")
    public PageResponseResult findNewsComments(@RequestBody NewsCommentsDto dto) {
        return apArticleService.findNewsComments(dto);
    }

    @Override
    @PostMapping("api/v1/article/statistics/newsDimension")
    public ResponseResult getNewsDimension(@RequestBody NewsDimensionDto dto) {
        return apArticleService.getNewsDimension(dto);
    }

    @Override
    @PostMapping("api/v1/statistics/newsPage")
    public PageResponseResult getNewsPage(@RequestBody NewsPageDto dto) {
        return apArticleService.getNewsPage(dto);
    }
}
