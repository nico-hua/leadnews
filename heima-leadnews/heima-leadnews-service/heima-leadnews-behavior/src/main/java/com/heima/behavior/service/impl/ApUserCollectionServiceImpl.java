package com.heima.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApUserCollectionService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApUserCollectionServiceImpl implements ApUserCollectionService {

    @Autowired
    private CacheService cacheService;
    @Override
    public ResponseResult collect(CollectionBehaviorDto dto) {
        // 1.登录校验
        String userId = ApThreadLocalUtil.getUser().getId().toString();
        // 2.参数校验
        if (dto == null||dto.getEntryId()==null||dto.getOperation()==null||dto.getType()==null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 3.判断是文章还是动态的收藏，拼字符串
        String key = dto.getType() == 0 ? BehaviorConstants.COLLECTION_ARTICLE + dto.getEntryId() :
                BehaviorConstants.COLLECTION_DYNAMIC + dto.getEntryId();
        // 4.判断是收藏还是取消收藏
        if (dto.getOperation() == 0) {
            cacheService.hPut(key, userId, JSON.toJSONString(dto));
        } else {
            cacheService.hDelete(key, userId);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}