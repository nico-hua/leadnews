package com.heima.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.UnLikeService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.UnLikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnLikeServiceImpl implements UnLikeService {
    @Autowired
    private CacheService cacheService;
    @Override
    public ResponseResult unlike(UnLikeBehaviorDto dto) {
        if(dto==null||dto.getArticleId()==null||dto.getType()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        String key = BehaviorConstants.UNLIKE_ARTICLE+dto.getArticleId();
        if(dto.getType() == 0){
            if(cacheService.hGet(key,userId)!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已加入不喜欢");
            }
            cacheService.hPut(key,userId, JSON.toJSONString(dto));
        }
        else{
            cacheService.hDelete(key,userId);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
