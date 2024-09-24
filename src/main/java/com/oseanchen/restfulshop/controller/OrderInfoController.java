package com.oseanchen.restfulshop.controller;

import com.oseanchen.restfulshop.dto.CreateOrderInfoRequest;
import com.oseanchen.restfulshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderInfoController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderInfoRequest createOrderInfoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Integer orderId = orderService.createOrder(userId, createOrderInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }
}
