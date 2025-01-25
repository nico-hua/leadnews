package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.dtos.WmSensitiveSaveDto;
import com.heima.model.wemedia.pojos.WmSensitive;

public interface WmSensitiveService extends IService<WmSensitive> {
    /**
     * 查询敏感词
     * */
    ResponseResult list(WmSensitiveDto wmSensitiveDto);

    /**
     * 删除敏感词
     * */
    ResponseResult del(Integer id);

    /**
     * 新增敏感词
     * */
    ResponseResult save(WmSensitiveSaveDto dto);

    /**
     * 修改敏感词
     * */
    ResponseResult update(WmSensitiveSaveDto dto);
}
