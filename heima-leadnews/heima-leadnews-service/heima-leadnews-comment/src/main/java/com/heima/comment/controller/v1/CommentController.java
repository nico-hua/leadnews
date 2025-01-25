package com.heima.comment.controller.v1;

import com.heima.comment.service.CommentService;
import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.CommentLoadDto;
import com.heima.model.comment.dto.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/load")
    public ResponseResult load(@RequestBody CommentLoadDto dto){
        return commentService.load(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody CommentSaveDto dto) throws Exception {
        return commentService.save(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }
}
