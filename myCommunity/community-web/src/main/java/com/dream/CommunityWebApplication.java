package com.dream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

//表示是个启动类
@SpringBootApplication
//包扫码注解
//@ComponentScan({"com.inxedu","springfox"})
//激活ConfigurationProperties注解
//@EnableConfigurationProperties({CommonConstants.class})
//mapper.xml扫描
@MapperScan({"com.dream.*.mapper"})
public class CommunityWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityWebApplication.class, args);
    }

}
