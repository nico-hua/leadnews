package com.heima.user.controller.v1;

import com.heima.user.service.ApUserFollowService;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/user_follow")
public class ApUserFollowController {
    @Autowired
    private ApUserFollowService userFollowService;
    @PostMapping
    public ResponseResult follow(@RequestBody UserRelationDto dto){
        return userFollowService.follow(dto);
    }

}