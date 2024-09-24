package com.oseanchen.restfulshop.dao;

import com.oseanchen.restfulshop.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoDao extends JpaRepository<OrderInfo, Integer> {
}
