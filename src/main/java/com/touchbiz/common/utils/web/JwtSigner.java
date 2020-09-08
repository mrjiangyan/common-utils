package com.touchbiz.common.utils.web;

import com.touchbiz.common.utils.tools.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtSigner {

    private final String KEY = "touchbiz";
    private final String issuer = "identity";

    @Value("${jwt.expiration.duration:604800}")
    private int duration;

    public String getAuthoritiesTag() {
        return "authorities";
    }

    public String getIssuerTag() {
        return issuer;
    }

    public String getTokenPrefix() {
        return "Bearer ";
    }

    public <T> String  generateToken(T user) {
        return Jwts.builder()
                .setAudience(JsonUtils.toJson(user))
                .setIssuer(issuer)
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(duration))))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, KEY.getBytes(UTF_8))
                .compact();
    }

    public <T> T parseToken(String token,Class<T> clazz) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY.getBytes(UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
            if(claims.getExpiration().before(new Date())){
                return null;
            }
            return JsonUtils.toObject(claims.getAudience(),clazz);
        } catch (SignatureException ex) {
            log.error("签名验证失败:{}",ex.getMessage());
            return null;
        }
    }
}
