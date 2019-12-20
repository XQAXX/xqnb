package com.dream.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.dream.common.intercepter.PageInterceptor;
import org.springframework.context.annotation.Bean;

public class MybatisPlusConfig {
    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PageInterceptor performanceInterceptor() {
        return new PageInterceptor();
    }
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
