package com.dream.config;
import com.dream.common.entity.shiro.Permission;
import com.dream.common.entity.shiro.Role;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.shiro.permission.PermissionService;
import com.dream.common.service.shiro.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义Realm
 * @author 小乔
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");
        /**
         * 判断ShiroRealm逻辑UsernamePasswordToken是否正确
         */
        //1判断用户名
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)authenticationToken;
        User user= null;
        try {
            user = userService.findByName(usernamePasswordToken.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //用户名不存在
        if(user==null){
            return null;
        }
        //如果账户被禁用则抛出异常
        if (user.getState()==2){
            throw new DisabledAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                //这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                user,
                //密码
                user.getPassword(),
                //salt=username+salt
                //ByteSource.Util.bytes(user.getCredentialsSalt()),
                //realm name
                getName()
        );
        //判断密码是否正确
        return authenticationInfo;
    }
    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        /**
         * 给资源授权
         */
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        //添加授权字符串
        //simpleAuthorizationInfo.addStringPermission("user:add");
        //--------------------认证账号
        Subject subject= SecurityUtils.getSubject();
        User user=(User)subject.getPrincipal();
        User user1=userService.findById(user.getUserId());
        //如果用户名不存在
        if(user1==null){

            return null;
        }
 /*       List<Role> roleList = user1.getRoleList();
        for ()
        simpleAuthorizationInfo.addRoles(roleList);*/
        //-------------------开始授权
        List<Permission> permissions =permissionService.getPermissionByUserId(user1.getUserId());
        for (Permission per : permissions) {
            simpleAuthorizationInfo.addStringPermission(per.getPermissionName());
            System.out.println("拥有权限："+per.getPermissionName());
        }
        return simpleAuthorizationInfo;
    }
}