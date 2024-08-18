package com.oseanchen.crudproject.controller;

import com.oseanchen.crudproject.dto.UserRequest;
import com.oseanchen.crudproject.model.User;
import com.oseanchen.crudproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/users/{id}")
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserByID(id);

        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
