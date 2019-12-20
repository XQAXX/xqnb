package com.dream.serviceimpl.shiro.user;
import com.dream.common.mapper.user.UserMapper;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.shiro.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findById(Long uId) {

        return null;
    }

    @Override
    public User findByName(String username) {
            User user = userMapper.findByName(username);
        return user;
    }
}
