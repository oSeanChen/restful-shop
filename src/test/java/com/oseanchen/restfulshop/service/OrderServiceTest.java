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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderInfoDao orderInfoDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Integer userId = 1;
        CreateOrderInfoRequest request = new CreateOrderInfoRequest();
        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(1);
        buyItem.setQuantity(2);
        request.setBuyItemList(Arrays.asList(buyItem));

        Product product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setUnitPrice(10.0);
        product.setUnitsInStock(5);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(1);
        orderInfo.setUserId(userId);
        orderInfo.setTotalAmount(20.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setOrderInfoId(1);
        orderItem.setProductId(1);
        orderItem.setQuantity(2);
        orderItem.setAmount(10.0);


        when(productDao.findById(1)).thenReturn(Optional.of(product));
        when(orderInfoDao.save(any())).thenReturn(orderInfo);
        when(orderItemDao.saveAll(any())).thenReturn(Arrays.asList(orderItem));

        // Act
        CreateOrderResponse response = orderService.createOrder(userId, request);

        // Assert
        assertNotNull(response);
        verify(productDao, times(1)).save(any());
        verify(productDao).save(argThat(savedProduct ->
                savedProduct.getId().equals(1) && savedProduct.getUnitsInStock() == 3
        ));

        verify(orderInfoDao, times(1)).save(any());
        verify(orderItemDao, times(1)).saveAll(any());

    }

    @Test
    void testCreateOrder_ProductNotFound() {
        Integer userId = 1;
        CreateOrderInfoRequest request = new CreateOrderInfoRequest();
        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(1);
        buyItem.setQuantity(2);
        request.setBuyItemList(Arrays.asList(buyItem));

        when(productDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> orderService.createOrder(userId, request));
    }

    @Test
    void testCreateOrder_InsufficientStock() {
        Integer userId = 1;
        CreateOrderInfoRequest request = new CreateOrderInfoRequest();
        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(1);
        buyItem.setQuantity(10);
        request.setBuyItemList(Arrays.asList(buyItem));

        Product product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setUnitPrice(10.0);
        product.setUnitsInStock(1);

        when(productDao.findById(1)).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.createOrder(userId, request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}