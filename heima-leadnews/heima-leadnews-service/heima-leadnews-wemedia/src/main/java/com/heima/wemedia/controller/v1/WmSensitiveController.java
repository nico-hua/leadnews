package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.dtos.WmSensitiveSaveDto;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmSensitiveDto dto){
        return wmSensitiveService.list(dto);
    }

    @DeleteMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") Integer id){
        return wmSensitiveService.del(id);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmSensitiveSaveDto dto){
        return wmSensitiveService.save(dto);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody WmSensitiveSaveDto dto){
        return wmSensitiveService.update(dto);
    }
}





















