package com.heima.behavior.controller.v1;

import com.heima.behavior.service.LikeService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/likes_behavior")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping
    public ResponseResult like(@RequestBody LikesBehaviorDto dto){
        return likeService.like(dto);
    }
}
