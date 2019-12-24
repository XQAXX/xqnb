package com.dream.common.service.shiro.user;

import com.dream.common.entity.page.PageEntity;
import com.dream.common.entity.shiro.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {
    User findById(Long uId);
    User findByName(String Username);
    List<User> findAll( PageEntity page);
}
