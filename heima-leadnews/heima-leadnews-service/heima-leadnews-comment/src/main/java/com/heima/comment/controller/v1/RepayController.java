package com.heima.comment.controller.v1;

import com.heima.comment.service.RepayService;
import com.heima.model.comment.dto.*;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment_repay")
public class RepayController {
    @Autowired
    private RepayService repayService;

    @PostMapping("/load")
    public ResponseResult load(@RequestBody RepayLoadDto dto){
        return repayService.load(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody RepaySaveDto dto) throws Exception {
        return repayService.save(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody RepayLikeDto dto){
        return repayService.like(dto);
    }
}
