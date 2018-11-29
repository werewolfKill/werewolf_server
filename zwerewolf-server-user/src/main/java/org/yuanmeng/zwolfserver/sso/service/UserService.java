package org.yuanmeng.zwolfserver.sso.service;

import org.springframework.stereotype.Service;
import org.yuanmeng.zwolfserver.sso.dao.UserRepository;
import org.yuanmeng.zwolfserver.sso.entity.UserEntity;

import javax.annotation.Resource;

/**
 * @author wangtonghe
 * @since 2018/11/30 00:34
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;


    public void addUser(UserEntity userEntity) throws Exception {
        userRepository.save(userEntity);

    }
}
