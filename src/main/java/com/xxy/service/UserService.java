package com.xxy.service;

import com.xxy.mapper.UserMapper;
import com.xxy.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public void saveUser(User user) {
        log.info("新增用户:"+user.toString());
        userMapper.insertSelective(user);
    }
}
