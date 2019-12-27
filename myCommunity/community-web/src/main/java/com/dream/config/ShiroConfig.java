package com.dream.config;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {

    /**
     * @describe 自定义凭证匹配器
     *由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了所以我们需要修改下doGetAuthenticationInfo中的代码）
     * 可以扩展凭证匹配器，实现输入密码错误次数后锁定等功能
     * @return org.apache.shiro.authc.credential.HashedCredentialsMatcher
     */
    @Bean
    public RetryLimitCredentialsMatcher hashedCredentialsMatcher(){
        RetryLimitCredentialsMatcher hashedCredentialsMatcher = new RetryLimitCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(1);
        //存储散列后的密码是否为16进制
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义权限认证
     * @return
     */
    @Bean("myShiroRealm")
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }
    @Bean("securityManager")
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver
    createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        //数据库异常处理
        mappings.setProperty("DatabaseException", "databaseError");
        mappings.setProperty("UnauthorizedException","/user/403");
        // None by default
        r.setExceptionMappings(mappings);
        // No default
        r.setDefaultErrorView("error");
        // Default is "exception"
        r.setExceptionAttribute("exception");
        return r;
    }
    /**
     * Shiro的过滤器链
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 没有登陆的用户只能访问登陆页面
        shiroFilterFactoryBean.setLoginUrl("/index");
        //设置未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        //添加Shiro拦截器
        /**
         * Shiro 内置过滤器，可以实现权限相关的拦截器
         *     anon:无需认证（登录）可以直接访问
         *     authc:必须认证才能访问
         *     user:如果使用rememberMe的功能才可以访问
         *     perms:该资源得到资源权限才可以访问
         *     role:该资源必须得到角色权限才可以访问画布
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login3", "anon");
        filterMap.put("/login", "anon");
        //百度编辑器
        filterMap.put("/static/**", "anon");
        filterMap.put("/file/**", "anon");
        filterMap.put("/ueditor/**","anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/upload/**", "anon");
        filterMap.put("/qiao/**", "anon");
        filterMap.put("/index", "anon");
        //添加Shiro授权拦截器
        filterMap.put("/add", "perms[添加]");
        filterMap.put("/foresee", "perms[预言未来]");
        filterMap.put("/update", "perms[修改]");
        filterMap.put("/delete", "perms[删除]");
        //对所有用户认证
        filterMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }
}
