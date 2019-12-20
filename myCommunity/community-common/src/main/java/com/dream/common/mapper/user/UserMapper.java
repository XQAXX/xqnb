package com.dream.common.mapper.user;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dream.common.entity.shiro.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

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
}
