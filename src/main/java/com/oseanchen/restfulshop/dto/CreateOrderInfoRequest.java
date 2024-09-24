package com.oseanchen.restfulshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderInfoRequest {

    @NotEmpty
    private List<BuyItem> buyItemList;
}
