package com.heima.wemedia.service;

import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;

import java.util.Date;

public interface StatisticsService {
    /**
     * 获取文章统计
     * */
    ResponseResult getNewsDimension(Date beginDate, Date endDate);

    /**
     * 获取文章
     * */
    PageResponseResult getNewsPage(Integer page, Integer size, Date beginDate, Date endDate);
}
