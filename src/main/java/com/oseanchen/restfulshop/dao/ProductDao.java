package com.oseanchen.restfulshop.dao;

import com.oseanchen.restfulshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p " +
            "WHERE p.productName like %:productName% " +
            "order by p.productName desc ")
    List<Product> findByProductName(@Param("productName") String productName);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
