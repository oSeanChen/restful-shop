package com.oseanchen.crudproject.config;

import com.oseanchen.crudproject.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

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
                        .requestMatchers(HttpMethod.DELETE, "/api/products").hasAuthority("ROLE_BUYER")
                        .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN") // 任何 /api/users 開頭的，且所有方法都算
//                        .requestMatchers(HttpMethod.GET, "/api/users/?*").hasAuthority("ROLE_ADMIN") // 只有 /api/users/{id} 才算
                        .anyRequest().authenticated()//其他尚未匹配到的路徑都需要身份驗證
                )
//                .httpBasic(Customizer.withDefaults()) // HTTP Basic 身份驗證作為默認的驗證方式
//                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
