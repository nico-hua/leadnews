package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {

    /**
     * 保存用户搜索历史记录
     * */
    void insert(String keyword, Integer userId);

    /**
     * 查询搜索历史
     * */
    ResponseResult findUserSearch();

    /**
     * 删除搜索历史
     * */
    ResponseResult delUserSearch(HistorySearchDto historySearchDto);
}
