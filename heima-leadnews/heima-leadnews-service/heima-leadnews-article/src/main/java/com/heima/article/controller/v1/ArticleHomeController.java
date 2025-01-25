package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleBehaviorDetailService;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
@Api(value = "app端文章展示",tags = "app端文章展示")
public class ArticleHomeController {
    @Autowired
    private ApArticleService apArticleService;

    @Autowired
    private ApArticleBehaviorDetailService articleBehaviorDetailService;

    /**
     * 加载首页文章列表
     */
    @PostMapping("/load")
    @ApiOperation("加载首页文章列表")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        //return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
        return apArticleService.load2(dto, ArticleConstants.LOADTYPE_LOAD_MORE, true);
    }

    /**
     * 加载更多
     */
    @PostMapping("/loadmore")
    @ApiOperation("加载更多")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载新文章
     */
    @PostMapping("/loadnew")
    @ApiOperation("加载新文章")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    @PostMapping("/load_article_behavior")
    @ApiOperation("文章详情")
    ResponseResult articleBehaviorDetail(@RequestBody ArticleInfoDto dto){
        return articleBehaviorDetailService.behaviorDetails(dto);
    }
}
