package com.heima.behavior.controller.v1;

import com.heima.behavior.service.UnLikeService;
import com.heima.model.behavior.dtos.UnLikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/un_likes_behavior")
public class UnLikeController {

    @Autowired
    private UnLikeService unLikeService;

    @PostMapping
    public ResponseResult unLike(@RequestBody UnLikeBehaviorDto dto) {
        return unLikeService.unlike(dto);
    }
}
