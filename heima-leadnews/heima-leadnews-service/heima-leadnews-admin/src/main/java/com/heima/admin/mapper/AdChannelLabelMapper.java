package com.heima.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.pojos.AdChannelLabel;
import com.heima.model.admin.vos.AdChannelVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdChannelLabelMapper extends BaseMapper<AdChannelLabel> {
    List<AdChannelVo> findAllAdChannelVo();
}
