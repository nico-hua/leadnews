package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.dtos.WmChannelSaveDto;
import com.heima.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    ResponseResult findAll();

    /**
     * 获取频道列表
     * */
    ResponseResult list(WmChannelDto dto);

    /**
     * 新增频道
     * */
    ResponseResult save(WmChannelSaveDto dto);

    /**
     * 删除频道
     * */
    ResponseResult del(Integer id);

    /**
     * 修改频道
     * */
    ResponseResult update(WmChannelSaveDto dto);
}
