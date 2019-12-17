package com.dream.common.entity.shiro;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class SysPermission {
    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    /**主键.*/
    private Integer permissionId;
    @Column(nullable = false)
    /**名称.*/
    private String permissionName;
    @Column(columnDefinition="enum('menu','button')")
    /**资源类型，[menu|button]*/
    private String resourceType;
    /**资源路径.*/
    private String url;
    /**权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view*/
    private String permission;
    /**父编号*/
    private Long parentId;
    /**父编号列表*/
    private String parentIds;
    private Boolean available = Boolean.TRUE;
    /**角色 -- 权限关系：多对多关系;*/
    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<SysRole> roles;

}
