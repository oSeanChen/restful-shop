package com.oseanchen.crudproject.dto;

import com.oseanchen.crudproject.model.Category;
import com.oseanchen.crudproject.model.Supplier;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductRequest {

    @NotNull
    private String productName;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Integer unitsInStock;

    @NotNull
    private Boolean discontinued;

    @NotNull
    private Category category;

    @NotNull
    private Supplier supplier;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}
