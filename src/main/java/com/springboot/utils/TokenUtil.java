package com.springboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {
    @Value("secretKey_for_WebNetDisk")
    private final String secretKey = "secretKey_for_WebNetDisk";
    /**
     * 加密token.
     */
    public String getToken(String user_id, String email) {
        //这个是放到负载payLoad 里面,魔法值可以使用常量类进行封装.
        String token = JWT
                .create()
                .withClaim("user_id", user_id)
                .withClaim("email", email)
                .withClaim("timeStamp", System.currentTimeMillis())
                .sign(Algorithm.HMAC256(secretKey));
        return token;
    }
    /**
     * 解析token.
     * {
     * "userId": "weizhong",
     * "userRole": "ROLE_ADMIN",
     * "timeStamp": "134143214"
     * }
     */
    public HashMap<String, String> parseToken(String token) {
        HashMap<String, String> map = new HashMap<String, String>();
        DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC256(secretKey))
                .build().verify(token);
        Claim user_id = decodedjwt.getClaim("user_id");
        Claim email = decodedjwt.getClaim("email");
        Claim timeStamp = decodedjwt.getClaim("timeStamp");
        map.put("user_id", user_id.asString());
        map.put("email", email.asString());
        map.put("timeStamp", timeStamp.asLong().toString());
        return map;
    }
}
