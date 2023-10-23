package com.springboot.configuration;

import com.springboot.interceptor.TokenHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Autowired
    TokenHandlerInterceptor tokenHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenHandlerInterceptor)
                .addPathPatterns("/user/*")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register");
    }
}
