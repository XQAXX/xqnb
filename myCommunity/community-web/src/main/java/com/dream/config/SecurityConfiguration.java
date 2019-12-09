package com.dream.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SecurityConfiguration配置拦截url的规则
 * @author 乔宏展
 */
@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
    }
}
