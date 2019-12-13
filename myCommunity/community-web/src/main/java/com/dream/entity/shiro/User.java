package com.dream.entity.shiro;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User implements Serializable {
    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer userId;
    /**登录用户名*/
    @Column(nullable = false, unique = true)
    private String userName;
    /**名称（昵称或者真实姓名，根据实际情况定义）*/
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    /**加密密码的盐*/
    private String salt;
    /**用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.*/
    private byte state;
    /**立即从数据库中进行加载数据;*/
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    /**一个用户具有多个角色*/
    private List<SysRole> roleList;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    /**创建时间*/
    private LocalDateTime createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /**过期日期*/
    private LocalDate expiredDate;
    private String email;
}
