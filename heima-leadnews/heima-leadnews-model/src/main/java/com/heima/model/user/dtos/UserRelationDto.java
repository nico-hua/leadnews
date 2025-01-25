package com.heima.model.user.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UserRelationDto {
    @IdEncrypt
    private Long articleId;
    private Integer authorId;
    private Short operation;
}