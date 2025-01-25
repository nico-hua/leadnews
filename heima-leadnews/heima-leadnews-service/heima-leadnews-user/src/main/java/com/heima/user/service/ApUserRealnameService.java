package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.ApUserRealnameDto;
import com.heima.model.user.dtos.ApUserAuthDto;
import com.heima.model.user.pojos.ApUserRealname;

public interface ApUserRealnameService extends IService<ApUserRealname> {

    /**
     * 列表查询
     * */
    ResponseResult list(ApUserRealnameDto dto);

    /**
     * 审核通过
     * */
    ResponseResult authPass(ApUserAuthDto dto);

    /**
     * 审核不通过
     * */
    ResponseResult authFail(ApUserAuthDto dto);
}
