package com.dream.common.service.login;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginService {
    /**用户登录*/
    Map<String,Object> userLogin(String userName, String password, HttpServletResponse response);

}
