package com.heima.wemedia.service;

import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.RepaySaveDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.CommentStatusDto;
import com.heima.model.wemedia.dtos.ListCommentsDto;
import com.heima.model.wemedia.dtos.NewsCommentsDto;

public interface CommentService {
    /**
     * 获取文章评论相关
     * */
    PageResponseResult findNewsComments(NewsCommentsDto dto);

    /**
     * 获取文章具体评论
     * */
    PageResponseResult list(ListCommentsDto dto);

    /**
     * 点赞评论
     * */
    ResponseResult like(CommentLikeDto dto);

    /**
     * 评论回复
     * */
    ResponseResult commentRepay(RepaySaveDto dto) throws Exception;

    /**
     * 更新文章评论状态
     * */
    ResponseResult updateCommentStatus(CommentStatusDto dto);

    /**
     * 删除评论
     * */
    ResponseResult delComment(String commentId);

    /**
     * 删除评论回复
     * */
    ResponseResult delCommentRepay(String commentRepayId);
}
