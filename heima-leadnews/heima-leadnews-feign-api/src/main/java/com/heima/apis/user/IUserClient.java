package com.heima.apis.user;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "leadnews-user")
public interface IUserClient {

    @PostMapping("/api/v1/user/info")
    ResponseResult findUserInfo(@RequestBody ApUser apUser);
}
