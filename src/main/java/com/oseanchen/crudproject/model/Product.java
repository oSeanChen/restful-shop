package com.oseanchen.crudproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product extends BaseEntity {
    private String productName;

    private Double unitPrice;

    private Integer unitsInStock;

    private Boolean discontinued = false;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Supplier supplier;

}
