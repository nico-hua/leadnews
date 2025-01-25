package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.service.AdChannelLabelService;
import com.heima.model.admin.pojos.AdChannelLabel;
import com.heima.admin.mapper.AdChannelLabelMapper;
import com.heima.model.admin.vos.AdChannelVo;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class AdChannelLabelServiceImpl extends ServiceImpl<AdChannelLabelMapper, AdChannelLabel> implements AdChannelLabelService{
    @Autowired
    private AdChannelLabelMapper adChannelLabelMapper;
    @Override
    public ResponseResult getAdChannel() {
        List<AdChannelVo> list = adChannelLabelMapper.findAllAdChannelVo();
        return ResponseResult.okResult(list);
    }
}
