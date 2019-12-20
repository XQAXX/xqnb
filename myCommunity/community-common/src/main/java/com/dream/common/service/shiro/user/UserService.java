package com.dream.common.service.shiro.user;

import com.dream.common.entity.shiro.User;

public interface UserService {
    User findById(Long uId);
    User findByName(String Username);
}
