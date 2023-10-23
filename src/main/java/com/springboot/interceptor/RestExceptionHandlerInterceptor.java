package com.springboot.interceptor;

import com.springboot.entity.ResultData;
import com.springboot.entity.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandlerInterceptor {
    /**
     * 默认全局异常处理。
     * @param e the e
     * @return ResultData
     * @RestControllerAdvice RestController的增强类，可用于实现全局异常处理器
     * @ExceptionHandler 统一处理某一类异常，从而减少代码重复率和复杂度，比如要获取自定义异常可以@ExceptionHandler(BusinessException.class)
     * @ResponseStatus 指定客户端收到的http状态码
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return ResultData.fail(ReturnCode.EMAIL_OR_PASSWORD_ERROR.getCode(), e.getMessage());
    }

}
