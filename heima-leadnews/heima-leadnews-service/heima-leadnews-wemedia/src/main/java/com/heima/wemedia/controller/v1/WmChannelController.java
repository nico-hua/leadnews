package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.dtos.WmChannelSaveDto;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {
    @Autowired
    private WmChannelService wmChannelService;

    /**
     * 获取全部频道
     * */
    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmChannelDto dto){
        return wmChannelService.list(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmChannelSaveDto dto){
        return wmChannelService.save(dto);
    }

    @GetMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") Integer id){
        return wmChannelService.del(id);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody WmChannelSaveDto dto){
        return wmChannelService.update(dto);
    }
}
