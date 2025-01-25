package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.pojos.AdChannelLabel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelLabelService extends IService<AdChannelLabel> {
    /**
     * 获取全部频道
     * */
    ResponseResult getAdChannel();
}
