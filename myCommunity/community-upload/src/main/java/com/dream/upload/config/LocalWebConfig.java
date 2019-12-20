package com.dream.upload.config;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class LocalWebConfig implements WebMvcConfigurer {
    @Value("${project.file.root}")
    private String fileRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //必须配置project.file.root
        if(StringUtils.isNotEmpty(fileRoot)){
            //结尾必须有 File.separator
            if (!fileRoot.endsWith("\\") && !fileRoot.endsWith("/")){
                fileRoot = fileRoot+ File.separator;
            }
        } else {
            throw new RuntimeException("config 'project.file.root' not null");
        }

        registry.addResourceHandler("/**")
                .addResourceLocations("file:"+fileRoot)
//                 .addResourceLocations("classpath:/resources/")
                   .addResourceLocations("classpath:/static/");
//                 .addResourceLocations("classpath:/public/")
    }
}
