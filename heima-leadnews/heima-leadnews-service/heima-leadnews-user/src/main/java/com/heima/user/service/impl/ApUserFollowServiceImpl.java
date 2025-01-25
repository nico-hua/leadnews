package com.heima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.user.service.ApUserFollowService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApUserFollowServiceImpl implements ApUserFollowService {

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult follow(UserRelationDto dto) {
        if (dto == null || dto.getArticleId()==null || dto.getAuthorId()==null || dto.getOperation()==null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        String key = BehaviorConstants.FOLLOW_AUTHOR + dto.getAuthorId();
        if (dto.getOperation() == 0) {
            cacheService.hPut(key, userId, JSON.toJSONString(dto));
        } else {
            cacheService.hDelete(key, userId);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
