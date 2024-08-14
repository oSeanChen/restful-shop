package com.oseanchen.crudproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "suppliers")
@Data
public class Supplier extends BaseEntity{
    private String supplierName;
}
