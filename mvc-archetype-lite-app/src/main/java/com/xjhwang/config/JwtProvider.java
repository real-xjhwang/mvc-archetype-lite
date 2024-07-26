package com.xjhwang.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt处理
 *
 * @author 黄雪杰 on 2024-07-11 16:05
 */
@Component
public class JwtProvider implements InitializingBean {
    
    @Value("${application.jwt.sign-key}")
    private String signKey;
    
    @Getter
    @Value("${application.jwt.issuer}")
    private String issuer;
    
    private SecretKey secretKey;
    
    public String encode(String subject, long ttlMillis, Map<String, Object> claims) {
        
        if (claims == null) {
            claims = new HashMap<>();
        }
        // 签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 签发
        JwtBuilder jwtBuilder = Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .subject(subject)
            .issuer(issuer)
            .signWith(secretKey, Jwts.SIG.HS256);
        // 过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            jwtBuilder.expiration(exp);
        }
        return jwtBuilder.compact();
    }
    
    public Claims decode(String token) {
        
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    
    @Override
    public void afterPropertiesSet() {
        
        secretKey = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));
    }
    
}
