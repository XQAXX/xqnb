package com.dream.controller.login;

import com.dream.common.base.BaseController;
import com.dream.common.util.StringUtils;
import com.dream.common.service.login.LoginService;
import com.dream.common.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping(value = "/login", produces = "application/json;charset=utf-8")
    @ResponseBody
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
        result = loginService.userLogin(userName, password,response);

        return result;
    }
}
