package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.dtos.WmSensitiveSaveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Override
    public ResponseResult list(WmSensitiveDto wmSensitiveDto) {
        if(wmSensitiveDto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        wmSensitiveDto.checkParam();
        IPage page = new Page(wmSensitiveDto.getPage(),wmSensitiveDto.getSize());
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(wmSensitiveDto.getName()!=null&&!"".equals(wmSensitiveDto.getName())){
            lambdaQueryWrapper.like(WmSensitive::getSensitives,wmSensitiveDto.getName());
        }
        lambdaQueryWrapper.orderByDesc(WmSensitive::getCreatedTime);
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(wmSensitiveDto.getPage(), wmSensitiveDto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
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
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"删除失败，敏感词不存在或已被删除");
        }
    }

    @Override
    public ResponseResult save(WmSensitiveSaveDto dto) {
        if(dto==null||"".equals(dto.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断敏感词是否已经存在
        WmSensitive wmSensitive = getOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, dto.getSensitives()));
        if(wmSensitive!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"敏感词已存在");
        }
        wmSensitive = new WmSensitive();
        wmSensitive.setSensitives(dto.getSensitives());
        wmSensitive.setCreatedTime(new Date());
        wmSensitive.setStatus(true);
        save(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(WmSensitiveSaveDto dto) {
        if(dto==null||dto.getId()==null||"".equals(dto.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmSensitive wmSensitive = getById(dto.getId());
        if(wmSensitive==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"敏感词不存在");
        }
        wmSensitive.setSensitives(dto.getSensitives());
        updateById(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
