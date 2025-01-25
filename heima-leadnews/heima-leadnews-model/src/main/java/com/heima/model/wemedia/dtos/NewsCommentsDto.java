package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class NewsCommentsDto extends PageRequestDto {
    private String beginDate;
    private String endDate;
}
