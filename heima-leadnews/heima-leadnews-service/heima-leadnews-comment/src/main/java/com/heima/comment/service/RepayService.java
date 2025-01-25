package com.heima.comment.service;

import com.heima.model.comment.dto.*;
import com.heima.model.common.dtos.ResponseResult;

public interface RepayService {
    /**
     * 加载评论回复
     * */
    ResponseResult load(RepayLoadDto dto);

    /**
     * 保存评论回复
     * */
    ResponseResult save(RepaySaveDto dto) throws Exception;
    /**
     * 点赞评论回复
     * */
    ResponseResult like(RepayLikeDto dto);
}
