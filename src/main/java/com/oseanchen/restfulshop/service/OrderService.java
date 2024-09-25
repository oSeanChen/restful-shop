package com.oseanchen.restfulshop.service;

import com.oseanchen.restfulshop.dao.OrderInfoDao;
import com.oseanchen.restfulshop.dao.OrderItemDao;
import com.oseanchen.restfulshop.dao.ProductDao;
import com.oseanchen.restfulshop.dto.BuyItem;
import com.oseanchen.restfulshop.dto.CreateOrderInfoRequest;
import com.oseanchen.restfulshop.dto.CreateOrderResponse;
import com.oseanchen.restfulshop.model.OrderInfo;
import com.oseanchen.restfulshop.model.OrderItem;
import com.oseanchen.restfulshop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    public CreateOrderResponse createOrder(Integer userId, CreateOrderInfoRequest createOrderInfoRequest) {

        // 計算價錢
        Double totalAmount = 0.0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderInfoRequest.getBuyItemList()) {
            Product product = productDao.findById(buyItem.getProductId())
                    .orElseThrow(() -> {
                        log.warn("productId: {} not found", buyItem.getProductId());
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    });

            if (product.getUnitsInStock() < buyItem.getQuantity()) {
                log.warn("productId: {} stock is not enough, remaining stock is {}, requested quantity is {}", buyItem.getProductId(), product.getUnitsInStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            double amount = (buyItem.getQuantity() * product.getUnitPrice());
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            // 扣庫存
            int updateStock = product.getUnitsInStock() - orderItem.getQuantity();
            product.setUnitsInStock(updateStock);
            productDao.save(product);

            orderItemList.add(orderItem);
        }

        // 存 orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setTotalAmount(totalAmount);
        OrderInfo orderInfoSaved = orderInfoDao.save(orderInfo);
        int orderInfoId = orderInfoSaved.getId();

        // 存多筆 orderItem
        orderItemList.forEach(item -> item.setOrderInfoId(orderInfoId));
        orderItemDao.saveAll(orderItemList);

        // response
        List<HashMap<String, Object>> responseOrderItemList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            HashMap<String, Object> map = new HashMap<>();
            Product product = productDao.findById(orderItem.getProductId()).get();

            map.put("orderItemId", orderItem.getId());
            map.put("orderInfoId", orderItem.getOrderInfoId());
            map.put("productId", orderItem.getProductId());
            map.put("quantity", orderItem.getQuantity());
            map.put("amount", orderItem.getAmount());
            map.put("productName", product.getProductName());
            responseOrderItemList.add(map);
        }

        CreateOrderResponse createOrderResponse = new CreateOrderResponse();
        createOrderResponse.setOrderInfo(orderInfoSaved);
        createOrderResponse.setOrderItemList(responseOrderItemList);

        return createOrderResponse;
    }
}
