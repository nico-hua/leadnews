package com.heima.model.comment.dto;

import lombok.Data;

@Data
public class CommentSaveDto {
    private Long articleId;
    private String content;
}
