package com.springboot.interceptor;

import com.springboot.entity.MyException;
import com.springboot.entity.ReturnCode;
import com.springboot.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Slf4j
@Component
public class TokenHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    TokenUtil tokenUtil;
    // Token刷新时间设置为10分钟=600000毫秒
    @Value("600000")
    private Long refreshTime;

    // Token过期时间设置为100分钟=6000000毫秒
    @Value("6000000")
    private Long expiresTime;
    /**
     * 权限认证的拦截操作.
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        log.info("=======进入拦截器========");
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //为空就返回错误
        String token = httpServletRequest.getHeader("token");
        if (null == token || "".equals(token.trim())) {
            int code = ReturnCode.LOST_TOKEN.getCode();
            String message = ReturnCode.LOST_TOKEN.getMessage();
            throw new MyException(code, message);
        }
//        log.info("==============token:" + token);
        Map<String, String> map;
        try {
            map = tokenUtil.parseToken(token);
        } catch (Exception e) {
            int code = ReturnCode.INVALID_TOKEN.getCode();
            String message = ReturnCode.INVALID_TOKEN.getMessage();
            throw new MyException(code, message);
        }

        String user_id = map.get("userId");
        String userRole = map.get("userRole");
//        System.out.println("userRole:"+userRole);
        long timeOfUse = System.currentTimeMillis() - Long.parseLong(map.get("timeStamp"));

        //1.判断 token 是否过期
        if (timeOfUse < refreshTime) {
            log.info("token验证成功");
            return true;
        }
        //超过token刷新时间，刷新 token
        else if (timeOfUse >= refreshTime && timeOfUse < expiresTime) {
            httpServletResponse.setHeader("token",tokenUtil.getToken(user_id,userRole));
            log.info("token刷新成功");
            return true;
        }
        //token过期就返回 token 无效.
        else {
            int code = ReturnCode.OVERDUE_TOKEN.getCode();
            String message = ReturnCode.OVERDUE_TOKEN.getMessage();
            throw new MyException(code, message);
        }
    }
}
