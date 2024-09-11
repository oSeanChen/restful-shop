package com.oseanchen.restfulshop.controller;

import com.oseanchen.restfulshop.dto.ErrorResponse;
import com.oseanchen.restfulshop.dto.UserLoginRequest;
import com.oseanchen.restfulshop.dto.UserLoginResponse;
import com.oseanchen.restfulshop.dto.UserRequest;
import com.oseanchen.restfulshop.model.User;
import com.oseanchen.restfulshop.service.JwtService;
import com.oseanchen.restfulshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserByID(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping("/register")
    public ResponseEntity<User> Register(@RequestBody @Valid UserRequest userRequest) {
        User createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authentication);
                return ResponseEntity.status(HttpStatus.OK).body(UserLoginResponse.of(token, authentication));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Login failed", Collections.singletonList("Authentication failed")));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Login failed", Collections.singletonList("Incorrect username or password")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Login failed", Collections.singletonList("An unexpected error occurred")));
        }
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserByID(id);

        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
