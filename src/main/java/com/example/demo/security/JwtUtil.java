package com.example.demo.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private static String SECRET_KEY;

    @Value("#{${jwt.expireTime}}")
    private long expireTime;

    private static SecretKey generateSecretKey() {
        byte[] encodedKey = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(encodedKey, "HmacSHA256");
    }

    // 로그인 서비스 던질 때
    public String createToken(String email) {

        SecretKey signingKey = generateSecretKey();

        if (expireTime <= 0) {
            throw new IllegalArgumentException("만료시간이 0보다 작거나 같습니다.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    public String getEmailFromToken(String token) {

        SecretKey signingKey = generateSecretKey();

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid or expired token
            return null;
        }
    }
}
