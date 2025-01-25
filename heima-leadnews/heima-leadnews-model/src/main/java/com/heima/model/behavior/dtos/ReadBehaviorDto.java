package com.heima.model.behavior.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ReadBehaviorDto {
    // 文章id
    @IdEncrypt
    private Long articleId;
    // 阅读次数
    private Integer count;
}
