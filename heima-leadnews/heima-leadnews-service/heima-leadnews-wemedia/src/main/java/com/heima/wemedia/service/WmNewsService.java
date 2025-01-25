package com.heima.wemedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 查询文章
     * @param dto
     * @return
     */
    ResponseResult findAll(WmNewsPageReqDto dto);

    ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsDto dto);

    /**
     * 查询文章列表
     * */
    ResponseResult findListVo(WmNewsPageReqDto dto);

    /**
     * 查询文章详情
     * */
    ResponseResult findOneVo(Integer id);


    /**
     * 人工审核不通过
     * */
    ResponseResult authFail(WmNewsAuthDto dto);

    /**
     * 人工审核通过
     * */
    ResponseResult authPass(WmNewsAuthDto dto);
}