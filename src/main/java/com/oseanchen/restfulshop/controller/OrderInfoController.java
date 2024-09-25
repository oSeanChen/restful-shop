package com.oseanchen.restfulshop.controller;

import com.oseanchen.restfulshop.dto.CreateOrderInfoRequest;
import com.oseanchen.restfulshop.dto.CreateOrderResponse;
import com.oseanchen.restfulshop.model.User;
import com.oseanchen.restfulshop.service.OrderService;
import com.oseanchen.restfulshop.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderInfoController {
    private static final Logger log = LoggerFactory.getLogger(OrderInfoController.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderInfoRequest createOrderInfoRequest) {

        Optional<User> user = userService.getUserByID(userId);
        if (!user.isPresent()) {
            log.warn("UserId: {} is not found", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        CreateOrderResponse createOrderResponse = orderService.createOrder(userId, createOrderInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
    }
}
