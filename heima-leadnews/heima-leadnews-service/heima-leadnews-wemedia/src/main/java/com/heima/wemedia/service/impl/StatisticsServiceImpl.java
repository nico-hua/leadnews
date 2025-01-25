package com.heima.wemedia.service.impl;

import com.heima.apis.article.IArticleClient;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsDimensionDto;
import com.heima.model.wemedia.dtos.NewsPageDto;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private IArticleClient articleClient;
    @Override
    public ResponseResult getNewsDimension(Date beginDate, Date endDate) {
        // 获取用户id
        WmUser wmUser = WmThreadLocalUtil.getUser();
        NewsDimensionDto dto = new NewsDimensionDto();
        dto.setWmUserId(wmUser.getId());
        dto.setBeginDate(beginDate);
        dto.setEndDate(endDate);
        return articleClient.getNewsDimension(dto);
    }

    @Override
    public PageResponseResult getNewsPage(Integer page, Integer size, Date beginDate, Date endDate) {
        WmUser wmUser = WmThreadLocalUtil.getUser();
        NewsPageDto dto = new NewsPageDto();
        dto.setPage(page);
        dto.setSize(size);
        dto.setBeginDate(beginDate);
        dto.setEndDate(endDate);
        dto.setWmUserId(wmUser.getId());
        return articleClient.getNewsPage(dto);
    }
}
