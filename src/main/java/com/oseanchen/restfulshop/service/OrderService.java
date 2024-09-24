package com.oseanchen.restfulshop.service;

import com.oseanchen.restfulshop.dao.OrderInfoDao;
import com.oseanchen.restfulshop.dao.OrderItemDao;
import com.oseanchen.restfulshop.dao.ProductDao;
import com.oseanchen.restfulshop.dto.BuyItem;
import com.oseanchen.restfulshop.dto.CreateOrderInfoRequest;
import com.oseanchen.restfulshop.model.OrderInfo;
import com.oseanchen.restfulshop.model.OrderItem;
import com.oseanchen.restfulshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    public Integer createOrder(Integer userId, CreateOrderInfoRequest createOrderInfoRequest) {

        // 計算價錢
        Double totalAmount = 0.0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderInfoRequest.getBuyItemList()) {
            Optional<Product> product = productDao.findById(buyItem.getProductId());

            double amount = (buyItem.getQuantity() * product.get().getUnitPrice());
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 存 orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setTotalAmount(totalAmount);
        OrderInfo orderInfoSaved = orderInfoDao.save(orderInfo);
        int orderInfoId = orderInfoSaved.getId();

        // 存多筆 orderItem
        for (OrderItem orderItem : orderItemList) {
            Product product = productDao.findById(orderItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 扣庫存
            int updateStock = product.getUnitsInStock() - orderItem.getQuantity();
            product.setUnitsInStock(updateStock);
            productDao.save(product);

            orderItem.setOrderInfoId(orderInfoId);
            orderItemDao.save(orderItem);
        }

        return orderInfoId;
    }
}
