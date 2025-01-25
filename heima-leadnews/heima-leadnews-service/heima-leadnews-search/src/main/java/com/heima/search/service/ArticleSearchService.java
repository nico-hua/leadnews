package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;

import java.io.IOException;

public interface ArticleSearchService {

    /**
     * ES 文章搜索
     * */
    ResponseResult search(UserSearchDto dto) throws IOException;
}
