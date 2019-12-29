package com.dream.common.mapper.user;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dream.common.entity.page.PageEntity;
import com.dream.common.entity.shiro.User;
import com.dream.common.entity.shiro.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("userMapper")
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据账号密码查询用户
     * @param userName
     * @param password
     * @return
     */
    User queryUser(@Param("userName") String userName,@Param("password")  String password);
    /**
     * 根据账号查询用户
     * @param userName
     * @return
     */
    User findByName(@Param("userName") String userName);
    /**
     * 根据账号id查询用户
     * @param userId
     * @return
     */
    UserDto findById(@Param("userId") Long userId);
    List<User> queryUser(PageEntity page);
}
