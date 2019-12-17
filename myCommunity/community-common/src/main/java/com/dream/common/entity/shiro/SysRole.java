package com.dream.common.entity.shiro;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
/**
 * shiro的角色
 */
@Entity
@Data
public class SysRole {
    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    /**编号*/
    private Integer roleId;
    @Column(nullable = false, unique = true)
    /**角色标识程序中判断使用,如"admin",这个是唯一的:*/
    private String role;
    /**角色描述,UI界面显示使用*/
    private String description;
    /**是否可用,如果不可用将不会添加给用户*/
    private Boolean available = Boolean.TRUE;

    /**角色 -- 权限关系：多对多关系;*/
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;

    /**用户 - 角色关系定义;*/
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="userId")})
    /**一个角色对应多个用户*/
    private List<User> users;
}
