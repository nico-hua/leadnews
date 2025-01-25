package com.heima.user.feign;

import com.heima.apis.user.IUserClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserClient implements IUserClient {
    @Autowired
    private ApUserService userService;
    @Override
    @PostMapping("/api/v1/user/info")
    public ResponseResult findUserInfo(@RequestBody ApUser apUser) {
        return userService.findUserInfo(apUser);
    }
}
