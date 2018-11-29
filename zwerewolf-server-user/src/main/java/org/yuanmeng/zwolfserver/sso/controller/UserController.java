package org.yuanmeng.zwolfserver.sso.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuanmeng.zwolfserver.sso.entity.UserEntity;
import org.yuanmeng.zwolfserver.sso.entity.response.ServiceResult;
import org.yuanmeng.zwolfserver.sso.service.UserService;

import javax.annotation.Resource;

/**
 * @author wangtonghe
 * @since 2018/11/30 00:30
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Resource
    private UserService userService;

    @RequestMapping(value = "/add")
    public ServiceResult addUser(@RequestBody UserEntity userEntity) throws Exception {

        ServiceResult result = new ServiceResult();


        userService.addUser(userEntity);

        result.setCode(0);
        return result;


    }
}
