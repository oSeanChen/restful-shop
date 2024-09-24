package com.oseanchen.restfulshop.dao;

import com.oseanchen.restfulshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
}
