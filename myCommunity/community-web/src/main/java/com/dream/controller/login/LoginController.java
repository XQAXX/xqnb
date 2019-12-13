package com.dream.controller.login;

import com.dream.common.base.BaseController;
import com.dream.service.login.LoginService;
import com.dream.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 网站上面用户登录
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

}
