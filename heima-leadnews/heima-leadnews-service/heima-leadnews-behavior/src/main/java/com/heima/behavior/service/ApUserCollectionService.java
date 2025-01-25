package com.heima.behavior.service;

import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApUserCollectionService {
    /**
     * 用户收藏文章
     */
    ResponseResult collect (CollectionBehaviorDto dto);
}
