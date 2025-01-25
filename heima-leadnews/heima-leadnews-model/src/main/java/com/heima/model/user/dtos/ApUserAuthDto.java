package com.heima.model.user.dtos;

import lombok.Data;

@Data
public class ApUserAuthDto {
    /**
     * id
     * */
    private Integer id;

    /**
     * 拒接原因
     * */
    private String msg;
}
