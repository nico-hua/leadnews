package com.heima.comment.service;

import com.heima.comment.pojos.ApComment;
import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.CommentLoadDto;
import com.heima.model.comment.dto.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.CommentStatusDto;

public interface CommentService {
    /**
     * 加载文章评论
     * */
    ResponseResult load(CommentLoadDto dto);

    /**
     * 保存文章评论
     * */
    ResponseResult save(CommentSaveDto dto) throws Exception;

    /**
     * 点赞评论
     * */
    ResponseResult like(CommentLikeDto dto);
}
