package com.cqyt.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @PROJECT_NAME: remote-sensing-admin
 * @DESCRIPTION:
 * @USER: Zg先生
 * @DATE: 2020/3/8 19:10
 */
@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 配置跨域请求
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedHeaders("*")  // 允许任何请求头
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

}
