package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelLabelService;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
    @Autowired
    private AdChannelLabelService adChannelLabelService;

    @GetMapping("/channels")
    public ResponseResult getChannel(){
        return adChannelLabelService.getAdChannel();
    }
}
