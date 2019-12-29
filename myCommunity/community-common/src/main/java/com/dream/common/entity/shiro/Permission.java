package com.dream.common.entity.shiro;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = -297287266522617580L;
    /**主键.*/
    private Long permissionId;
    /**名称.*/
    private String permissionName;
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
    @TableField(exist = false)
    private List<Role> roles;

}
