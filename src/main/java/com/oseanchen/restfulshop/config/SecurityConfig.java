package com.oseanchen.restfulshop.config;

import com.oseanchen.restfulshop.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.function.Supplier;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests((registry) -> registry
                                .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/error", "/api/products/**", "/who-am-i").permitAll()   //指定路徑允許所有用戶訪問，不需身份驗證
                                .requestMatchers(HttpMethod.GET, "/checkAuthentication").hasAnyAuthority("ROLE_BUYER", "ROLE_SELLER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("ROLE_SELLER")
                                .requestMatchers(HttpMethod.DELETE, "/api/products").hasAuthority("ROLE_SELLER")
                                .requestMatchers("/api/users/*").hasAuthority("ROLE_ADMIN") // 任何 /api/users 開頭的，且所有方法都算
//                        .requestMatchers(HttpMethod.POST, "/api/users/*/orders").hasAnyAuthority("ROLE_BUYER")
                                .requestMatchers(HttpMethod.POST, "/api/users/{userId}/orders").access(this::checkUserIdAndRole)
                                .anyRequest().authenticated()//其他尚未匹配到的路徑都需要身份驗證
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private AuthorizationDecision checkUserIdAndRole(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        int userId = Integer.parseInt(context.getVariables().get("userId"));
        Authentication auth = authentication.get();
        // 轉型問題處理
        if (!(auth.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            return new AuthorizationDecision(false);
        }

        boolean hasAccess = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                // 當前 userId 對應路徑 userId 才允許授權
                (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BUYER")) && userPrincipal.getId().equals(userId));

        log.info("checkUserIdAndRole access permit: {}", hasAccess);
        return new AuthorizationDecision(hasAccess);
    }
}
