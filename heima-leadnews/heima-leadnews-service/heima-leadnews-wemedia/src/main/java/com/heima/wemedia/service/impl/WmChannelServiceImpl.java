package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.dtos.WmChannelSaveDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    @Override
    public ResponseResult list(WmChannelDto dto) {
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dto.getName()!=null&&!"".equals(dto.getName())){
            lambdaQueryWrapper.like(WmChannel::getName, dto.getName());
        }
        lambdaQueryWrapper.orderByAsc(WmChannel::getOrd);
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult save(WmChannelSaveDto dto) {
        if(dto==null||"".equals(dto.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmChannel wmChannel = getOne(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName, dto.getName()));
        if(wmChannel!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"频道已存在");
        }
        wmChannel = new WmChannel();
        BeanUtils.copyProperties(dto,wmChannel);
        wmChannel.setIsDefault(true);
        wmChannel.setCreatedTime(new Date());
        save(wmChannel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult del(Integer id) {
        if(id==null||id<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean flag = removeById(id);
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"删除失败，频道不存在或已被删除");
        }
    }

    @Override
    public ResponseResult update(WmChannelSaveDto dto) {
        if(dto==null||dto.getId()==null||"".equals(dto.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmChannel wmChannel = getById(dto.getId());
        if(wmChannel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"频道不存在");
        }
        wmChannel.setName(dto.getName());
        wmChannel.setDescription(dto.getDescription());
        wmChannel.setOrd(dto.getOrd());
        wmChannel.setStatus(dto.getStatus());
        updateById(wmChannel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
