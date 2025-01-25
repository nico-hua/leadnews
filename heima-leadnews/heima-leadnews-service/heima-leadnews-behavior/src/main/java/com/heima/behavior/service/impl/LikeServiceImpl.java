package com.heima.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.LikeService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {
    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        // 参数校验
        if(dto == null || dto.getArticleId() == null || dto.getOperation() == null || dto.getType() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 获取登录用户id
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        // 拼接key
        String key = "";
        if(dto.getType() == 0){
            key = BehaviorConstants.LIKE_ARTICLE+dto.getArticleId();
        }
        if(dto.getType() == 1){
            key = BehaviorConstants.LIKE_DYNAMIC+dto.getArticleId();
        }
        if(dto.getType() == 2){
            key = BehaviorConstants.LIKE_COMMENT+dto.getArticleId();
        }
        // 判断是点赞还是取消点赞
        if(dto.getOperation()==0){
            if(cacheService.hGet(key,userId)!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已点赞，请勿重复操作");
            }
            cacheService.hPut(key, userId, JSON.toJSONString(dto));
        }
        else{
            cacheService.hDelete(key,userId);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}



























