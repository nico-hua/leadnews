package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmSensitiveDto extends PageRequestDto {
    /**
     * 关键词
     * */
    private String name;

}
