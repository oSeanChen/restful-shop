package com.oseanchen.restfulshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderInfoId;
    private Integer productId;
    private Integer quantity;
    private Double amount;
}
