package com.heima.behavior.service;

import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ReadService {

    /**
     * 阅读次数计数
     * */
    ResponseResult read(ReadBehaviorDto dto);
}
