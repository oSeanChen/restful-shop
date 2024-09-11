package com.oseanchen.restfulshop.dto;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class UserLoginResponse {
    private String token;
    private String username;
    private List<String> authorities;

    public static UserLoginResponse of(String token, Authentication authentication) {
        UserLoginResponse res = new UserLoginResponse();
        res.token = token;
        res.username = authentication.getName();
        res.authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return res;
    }
}
