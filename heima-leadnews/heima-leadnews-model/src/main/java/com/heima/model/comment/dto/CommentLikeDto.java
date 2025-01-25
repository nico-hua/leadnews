package com.heima.model.comment.dto;

import lombok.Data;

@Data
public class CommentLikeDto {
    private String commentId;
    private Short operation;
}
