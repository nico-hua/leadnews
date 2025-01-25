package com.heima.wemedia.controller.v1;

import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.RepaySaveDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.CommentStatusDto;
import com.heima.model.wemedia.dtos.ListCommentsDto;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.wemedia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/manage/find_news_comments")
    public PageResponseResult findNewsComments(@RequestBody NewsCommentsDto dto) {
        return commentService.findNewsComments(dto);
    }

    @PostMapping("/manage/list")
    public PageResponseResult list(@RequestBody ListCommentsDto dto){
        return commentService.list(dto);
    }

    @PostMapping("/manage/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }

    @PostMapping("/manage/comment_repay")
    public ResponseResult commentRepay(@RequestBody RepaySaveDto dto) throws Exception {
        return commentService.commentRepay(dto);
    }

    @PostMapping("/manage/update_comment_status")
    public ResponseResult updateCommentStatus(@RequestBody CommentStatusDto dto){
        return commentService.updateCommentStatus(dto);
    }

    @DeleteMapping("/manage/del_comment/{commentId}")
    public ResponseResult delComment(@PathVariable("commentId") String commentId){
        return commentService.delComment(commentId);
    }

    @DeleteMapping("/manage/del_comment_repay/{commentRepayId}")
    public ResponseResult delCommentRepay(@PathVariable("commentRepayId") String commentRepayId){
        return commentService.delCommentRepay(commentRepayId);
    }
}




















