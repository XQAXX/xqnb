package com.dream.config;

import com.dream.common.intercepter.IntercepterWebLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * @author 小乔
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private IntercepterWebLogin intercepterWebLogin;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  前台用户登录拦截器配置
        registry.addInterceptor(intercepterWebLogin)
                .addPathPatterns("/**/*")
                .excludePathPatterns("/login");
    }
}
