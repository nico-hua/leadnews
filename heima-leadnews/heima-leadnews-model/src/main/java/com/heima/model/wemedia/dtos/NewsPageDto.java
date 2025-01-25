package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

@Data
public class NewsPageDto extends PageRequestDto {
    private Integer wmUserId;
    private Date beginDate;
    private Date endDate;
}
