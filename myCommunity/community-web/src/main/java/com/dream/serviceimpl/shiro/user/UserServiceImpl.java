package com.dream.serviceimpl.shiro.user;
import com.dream.common.entity.page.PageEntity;
import com.dream.common.entity.shiro.UserDto;
import com.dream.common.mapper.user.UserMapper;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.shiro.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;


    @Override
    public User findByName(String username) {
            User user = userMapper.findByName(username);
        return user;
    }

    @Override
    public List<User> queryUser(PageEntity page) {
        return userMapper.queryUser(page);
    }

    @Override
    public UserDto findById(Long userId) {
        return userMapper.findById(userId);
    }
}
