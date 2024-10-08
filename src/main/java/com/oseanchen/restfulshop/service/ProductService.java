package com.oseanchen.restfulshop.service;

import com.oseanchen.restfulshop.dao.ProductDao;
import com.oseanchen.restfulshop.dto.ProductRequest;
import com.oseanchen.restfulshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public Optional<Product> getProductById(int id) {
        return productDao.findById(id);
    }

    @Transactional
    public Product updateProduct(Integer id, ProductRequest productRequest) {
        Optional<Product> product = getProductById(id);
        if (product.isPresent()) {
            Product updatedProduct = convertToModel(productRequest);
            updatedProduct.setId(id);
            return productDao.save(updatedProduct);
        }
        return null;
    }

    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        Product product = convertToModel(productRequest);
        return productDao.save(product);
    }

    @Transactional
    public void deleteProductById(Integer id) {
        productDao.deleteById(id);
    }

    public List<Product> searchAndSortProducts(String productName, String sortBy, String sortOrder, int page, int limit) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<Product> productPage = null;
        if (productName == null || productName.isEmpty()) {
            productPage = productDao.findAll(pageable);
        } else {
            productPage = productDao.findByProductNameContainingIgnoreCase(productName, pageable);
        }

        List<Product> products = productPage.getContent();
        return products;
    }

    public Product convertToModel(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setUnitPrice(productRequest.getUnitPrice());
        product.setUnitsInStock(productRequest.getUnitsInStock());
        product.setDiscontinued(productRequest.getDiscontinued());
        product.setSupplier(productRequest.getSupplier());
        return product;
    }
}
