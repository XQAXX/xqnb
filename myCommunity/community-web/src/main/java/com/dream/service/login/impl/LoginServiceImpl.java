package com.dream.service.login.impl;

import com.dream.common.constans.UserCacheConstans;
import com.dream.common.service.cache.Cache;
import com.dream.common.service.cache.CookieUtils;
import com.dream.common.util.ObjectUtils;
import com.dream.common.dao.user.UserDao;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private UserDao userDao;
    @Autowired
    private Cache cache;

    @Override
    public Map<String, Object> userLogin(String userName, String password, HttpServletResponse response) {
        User user = userDao.queryUser(userName, password);
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
        if (user.getState() == 3) {
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
            result.put("success", true);
            result.put("message", "登录成功！");
            result.put("entity", "");

        return result;
    }
}
