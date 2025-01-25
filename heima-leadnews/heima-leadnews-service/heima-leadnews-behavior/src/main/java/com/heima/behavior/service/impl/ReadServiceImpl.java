package com.heima.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ReadService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadServiceImpl implements ReadService {
    @Autowired
    private CacheService cacheService;
    @Override
    public ResponseResult read(ReadBehaviorDto dto) {
        if(dto == null|| dto.getArticleId() == null||dto.getCount()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        String key = BehaviorConstants.READ_ARTICLE+dto.getArticleId();
        String redisString = (String) cacheService.hGet(key, userId);
        if(redisString!=null){
            ReadBehaviorDto readBehaviorDto = JSON.parseObject(redisString, ReadBehaviorDto.class);
            dto.setCount(dto.getCount()+readBehaviorDto.getCount());
        }
        cacheService.hPut(key,userId,JSON.toJSONString(dto));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}














