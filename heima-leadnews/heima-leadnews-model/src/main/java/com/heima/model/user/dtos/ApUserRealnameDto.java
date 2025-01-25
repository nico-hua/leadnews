package com.heima.model.user.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class ApUserRealnameDto extends PageRequestDto {
    /**
     * 状态
     * */
    private Short status;
}
