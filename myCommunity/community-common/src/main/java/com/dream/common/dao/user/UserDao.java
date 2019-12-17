package com.dream.common.dao.user;

import com.dream.common.entity.shiro.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User,Integer> {
    @Query(value = "select * from user  where user_name =:userName and password =:password",nativeQuery=true)
    User queryUser(@Param("userName") String userName,@Param("password")  String password);
}
