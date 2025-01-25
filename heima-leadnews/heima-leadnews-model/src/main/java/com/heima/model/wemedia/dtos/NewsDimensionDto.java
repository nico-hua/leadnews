package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class NewsDimensionDto {
    private Integer wmUserId;
    private Date beginDate;
    private Date endDate;
}
