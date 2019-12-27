package com.dream.controller.login;

import com.dream.common.base.BaseController;
import com.dream.common.entity.page.PageEntity;
import com.dream.common.entity.shiro.User;
import com.dream.common.util.StringUtils;
import com.dream.common.service.login.LoginService;
import com.dream.common.service.shiro.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 网站上面用户登录
 */
@Controller
public class LoginController extends BaseController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    /**
     * 跳转到用户的登录界面
     * @return
     */
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }
    @RequestMapping(value = "/index2")
    public String inde2x() {
        return "log2in";
    }
    @ResponseBody
    @RequestMapping(value = "/login2", produces = "application/json;charset=utf-8")
    public Map<String, Object> login2(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result;
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(userName)) {
            result = this.ajaxResult(false, "账号不能为空", "");
            return result;
        }
        if (StringUtils.isBlank(password)) {
            result = this.ajaxResult(false, "密码不能为空", "");
            return result;
        }
        try {
            result = loginService.userLogin(userName, password,response);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return this.ajaxResult(false, "网络繁忙!", "");
        }
    }
    @ResponseBody
    @RequestMapping(value = "/login", produces = "application/json;charset=utf-8")
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result;
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(userName)) {
            result = this.ajaxResult(false, "账号不能为空", "");
            return result;
        }
        if (StringUtils.isBlank(password)) {
            result = this.ajaxResult(false, "密码不能为空", "");
            return result;
        }
        try {
            // 1.获取Subject
            Subject subject = SecurityUtils.getSubject();
            Object principal = subject.getPrincipal();
            // 2.封装用户数据
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            //设置记住当前用户
            token.setRememberMe(true);
            // 3.执行登录方法
            try{
                subject.login(token);
                return this.ajaxResult(true,"登录成功！","");
            } catch (UnknownAccountException e){
                return this.ajaxResult(false,"用户名不存在!","");
            } catch (IncorrectCredentialsException e){
                return this.ajaxResult(false,"密码错误!","");
            }catch (LockedAccountException lock){
                return this.ajaxResult(false,"帐号锁定!","");
            }catch (DisabledAccountException disa){
                return this.ajaxResult(false,"用户禁用!","");
            }catch (ExcessiveAttemptsException exce){
                return this.ajaxResult(false,"登录重试次数，超限。只允许在一段时间内允许有一定数量的认证尝试!","");
            }catch (ConcurrentAccessException  con){
               return this.ajaxResult(false,"一个用户多次登录异常：不允许多次登录，只能登录一次 。即不允许多处登录","");
            }catch (AccountException  acc){
                return this.ajaxResult(false,"账户异常!","");
            }catch (ExpiredCredentialsException  expir){
                return this.ajaxResult(false,"过期的凭据异常!","");
            }catch (CredentialsException  cre){
                return this.ajaxResult(false,"凭据异常!","");
            }catch (ShiroException shiro){
                shiro.printStackTrace();
                return this.ajaxResult(false,"shiro的全局异常!","");
            }
        }catch (Exception e){
            e.printStackTrace();
            return this.ajaxResult(false, "网络繁忙!", "");
        }
    }
    @RequestMapping(value = "/login3", produces = "application/json;charset=utf-8")
    public String login3(HttpServletRequest request, HttpServletResponse response) {
        // 1.获取Subject
        PageEntity page=new PageEntity();
        page.setCurrentPage(1);
        page.setPageSize(2);

        User user=new User();
        try {
            user.selectAll();
            userService.queryUser(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "log2in";
    }
    }
