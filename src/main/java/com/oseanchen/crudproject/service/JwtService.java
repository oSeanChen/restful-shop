package com.oseanchen.crudproject.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY_STR;

    @Value("${jwt.valid-seconds}")
    private int expirationTime;

    public String generateToken(Authentication authentication) {

        Map<String, Object> claims = new HashMap<>();

        Key key = Keys.hmacShaKeyFor(JWT_SECRET_KEY_STR.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * expirationTime * 3))
                .signWith(key, SignatureAlgorithm.HS256).compact();

    }

}
