package com.heima.user.service;

import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApUserFollowService {
    /**
     * 用户关注/取消关注作者
     */
    ResponseResult follow(UserRelationDto dto);
}
