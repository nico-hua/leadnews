package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApUserCollectionService;
import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/collection_behavior")
public class ApCollectionController {

    @Autowired
    private ApUserCollectionService apUserCollectionService;


    @PostMapping
    public ResponseResult collect(@RequestBody CollectionBehaviorDto dto) {
        return apUserCollectionService.collect(dto);
    }
}