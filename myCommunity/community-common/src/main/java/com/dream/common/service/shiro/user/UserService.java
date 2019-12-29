package com.dream.common.service.shiro.user;

import com.dream.common.entity.page.PageEntity;
import com.dream.common.entity.shiro.User;
import com.dream.common.entity.shiro.UserDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {

    User findByName(String Username);
    List<User> queryUser(PageEntity page);
    UserDto findById(Long userId);
}
