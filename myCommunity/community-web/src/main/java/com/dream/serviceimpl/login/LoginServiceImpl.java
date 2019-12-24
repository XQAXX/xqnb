package com.dream.serviceimpl.login;

import com.dream.common.constans.UserCacheConstans;
import com.dream.common.mapper.user.UserMapper;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.cache.Cache;
import com.dream.common.service.cache.CookieUtils;
import com.dream.common.service.login.LoginService;
import com.dream.common.util.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户登录实现类
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userDao;
    @Autowired
    private Cache cache;

    @Override
    public Map<String, Object> userLogin(String userName, String password, HttpServletResponse response) {
        //User user = userDao.queryUser(userName, password);
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        // 3.执行登录方法
        try{
             subject.login(token);
        } catch (UnknownAccountException e){
            Map<String, Object> result=new HashMap<>();
            result.put("success", false);
            result.put("message", "用户名不存在!");
            result.put("entity", "");
            return result;
        } catch (IncorrectCredentialsException e){
            Map<String, Object> result=new HashMap<>();
            result.put("success", false);
            result.put("message", "密码错误!");
            result.put("entity", "");
            return result;
    }catch (LockedAccountException lock){
        System.out.println("帐号锁定");
    }catch (DisabledAccountException disa){
        System.out.println("用户禁用");
    }catch (ExcessiveAttemptsException exce){
        System.out.println("登录重试次数，超限。只允许在一段时间内允许有一定数量的认证尝试");
    }catch (ConcurrentAccessException  con){
        System.out.println("一个用户多次登录异常：不允许多次登录，只能登录一次 。即不允许多处登录");
    }catch (AccountException  acc){
        System.out.println("账户异常");
    }catch (ExpiredCredentialsException  expir){
        System.out.println("过期的凭据异常");
    }catch (CredentialsException  cre){
        System.out.println("凭据异常");
    }catch (ShiroException shiro){
        shiro.printStackTrace();
        System.out.println("shiro的全局异常");
    }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> result = new HashMap<>();
        if (ObjectUtils.isNull(user)) {
            result.put("success", false);
            result.put("message", "登陆失败");
            result.put("entity", user);
            return result;
        }
        if (user.getState() == 0) {
            result.put("success", false);
            result.put("message", "抱歉您还没有操作的权限！,请联系管理员开通！");
            result.put("entity", "");
            return result;
        }
        if (user.getState() == 2) {
            result.put("success", false);
            result.put("message", "抱歉您的账号已经被锁定！无法继续操作！");
            result.put("entity", "");
            return result;
        }
        if (user.getState() == 1) {
            result.put("success", true);
            result.put("message", "登录成功！");
            result.put("entity", "");
            //随机获取一个uuid
            UUID uuid = UUID.randomUUID();
            //缓存用户
            cache.set(uuid.toString(), user,UserCacheConstans.USER_LOGIN_TIME);
            //缓存用户cookie key(后台获取后，可以清除登录用户的缓存)
            cache.set(UserCacheConstans.USER_LOGIN+user.getUserId(), uuid,UserCacheConstans.USER_LOGIN_TIME);
            //把redis的key存放到cookie里面
            CookieUtils.setCookie(response,UserCacheConstans.USER_LOGIN,uuid.toString(),UserCacheConstans.USER_LOGIN_TIME);
            return result;
        }
        return result;
    }
}
