package com.springboot.interceptor;

import com.alibaba.fastjson2.JSON;
import com.springboot.entity.ResultData;
import com.springboot.entity.ReturnCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;



@ControllerAdvice // 通过注解进行过滤哪些请求响应会被拦截，避免错误拦截。
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        /**
         * 我们可以选择哪些方法或者类进入beforeBodyWrite方法
         * 从returnType获取类名和方法名
         * 通过returnType.getMethod().getDeclaringClass.getName获取类名
         * converterType 表示当前请求使用的一个数据转换器，根据我们在controller指定返回类型决定，这里有个问题点待会会说
         */

        // 当控制层方法或者类上有标记注解 @ResponseAdvice直接时，才会进入beforeBodyWrite方法。当不需要任何限制时，supports直接返回true即可。
        RestController restController= returnType.getMethodAnnotation(RestController.class);
        if (restController == null) {
            restController = returnType.getDeclaringClass().getAnnotation(RestController.class);
        }
        return restController!= null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResultData) {
            return body;
        }
        if (body instanceof String) {
            System.out.println(JSON.toJSONString(ResultData.success(body)));
            return JSON.toJSONString(ResultData.success(body));
        }
        return ResultData.success(body);

    }
}
