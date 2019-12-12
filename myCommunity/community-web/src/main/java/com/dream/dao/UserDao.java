package com.dream.dao;

import com.dream.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface UserDao extends JpaRepository<User,Integer> {
    @Query(nativeQuery = true, value = "select u_id,u_name from qiao_user")
    public List<User> showUserList();
/*    @Insert(value = "insert into qiao_user (u_name) values(user.uName)")
    public void addUser(User user);*/
}
