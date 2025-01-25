package com.heima.behavior.service;

import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface LikeService {

    /**
     * 用户点赞
     * */
    ResponseResult like(LikesBehaviorDto dto);
}
