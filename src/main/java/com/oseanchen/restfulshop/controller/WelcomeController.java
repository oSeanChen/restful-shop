package com.oseanchen.restfulshop.controller;


import com.oseanchen.restfulshop.service.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WelcomeController {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @GetMapping("/checkAuthentication")
    public Authentication welcome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return authentication;
    }

    @GetMapping("/extractJwt")
    public Map<String, Object> extractJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.substring(BEARER_PREFIX.length());
        try {
            return jwtService.extractAllClaims(token);
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }
}
