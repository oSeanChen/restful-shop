package com.oseanchen.restfulshop.dto;

import com.oseanchen.restfulshop.model.OrderInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CreateOrderResponse {
    private OrderInfo orderInfo;
    private List<HashMap<String, Object>> orderItemList;
}
