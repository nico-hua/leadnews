package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

import java.io.IOException;

public interface ArticleFreemarkerService {

    public void buildArticleToMinIO(ApArticle apArticle, String content) throws IOException;
}
