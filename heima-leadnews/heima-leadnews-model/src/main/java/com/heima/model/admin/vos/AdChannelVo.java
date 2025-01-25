package com.heima.model.admin.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.heima.model.admin.pojos.AdChannelLabel;
import lombok.Data;

@Data
public class AdChannelVo {

    private Integer id;

    private Integer channelId;

    private Integer labelId;

    private Integer ord;
    private String name;
}
