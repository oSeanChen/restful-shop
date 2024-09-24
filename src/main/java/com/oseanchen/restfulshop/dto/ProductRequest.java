package com.oseanchen.restfulshop.dto;

import com.oseanchen.restfulshop.model.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductRequest {

    @NotBlank(message = "ProductName cannot be blank")
    private String productName;

    @NotNull
    @PositiveOrZero(message = "UnitPrice must be zero or positive")
    private Double unitPrice;

    @NotNull
    @PositiveOrZero(message = "UnitInStock must be zero or positive")
    private Integer unitsInStock;

    @NotNull
    private Boolean discontinued;

    @NotNull
    private Supplier supplier;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}
