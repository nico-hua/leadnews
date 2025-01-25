package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import com.heima.wemedia.service.StatisticsService;
import org.apache.hadoop.hbase.client.Append;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/newsDimension")
    public ResponseResult getNewsStatistics(
           @RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
           @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return statisticsService.getNewsDimension(beginDate, endDate);
    }

    @GetMapping("/newsPage")
    public PageResponseResult getNewsPage(@RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size,
                                          @RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
                                          @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return statisticsService.getNewsPage(page, size, beginDate, endDate);
    }
}
