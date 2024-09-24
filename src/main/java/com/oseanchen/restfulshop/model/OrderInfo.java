package com.oseanchen.restfulshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "order_info")
public class OrderInfo extends BaseEntity {
    private Integer userId;
    private Double totalAmount;
}
