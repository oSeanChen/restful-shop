package com.oseanchen.crudproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category extends BaseEntity{
    private String categoryName;
}
