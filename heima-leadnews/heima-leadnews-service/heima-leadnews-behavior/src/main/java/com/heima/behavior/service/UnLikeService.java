package com.heima.behavior.service;

import com.heima.model.behavior.dtos.UnLikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface UnLikeService {
    /**
     * 用户不喜欢
     * @param dto
     * @return
     */
    ResponseResult unlike(UnLikeBehaviorDto dto);
}