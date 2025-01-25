package com.heima.model.comment.dto;

import lombok.Data;

@Data
public class CommentLoadDto {
    private Long articleId;
    private Integer index;
    private Long minDate;
}
