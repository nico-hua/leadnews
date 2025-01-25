package com.heima.model.wemedia.dtos;

import lombok.Data;

@Data
public class WmChannelSaveDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean status;

    private Integer ord;
}
