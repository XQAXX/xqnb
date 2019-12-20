package com.dream.common.entity.shiro;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
/**
 * shiro的角色
 */
@Data
public class Role extends Model<Role> implements Serializable {
    private static final long serialVersionUID = -29728762265617580L;
    /**编号*/
    private Long roleId;
    /**角色标识程序中判断使用,如"admin",这个是唯一的:*/
    private String role;
    /**角色描述,UI界面显示使用*/
    private String description;
    /**是否可用,如果不可用将不会添加给用户*/
    private Boolean available = Boolean.TRUE;
    /**角色 -- 权限关系：多对多关系;*/
   private List<Permission> permissions;

    /**用户 - 角色关系定义;*/
    /**一个角色对应多个用户*/
    private List<User> users;

    @Override
    protected Serializable pkVal() {
        return roleId;
    }
}
