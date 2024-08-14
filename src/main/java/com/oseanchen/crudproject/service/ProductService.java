package com.oseanchen.crudproject.service;

import com.oseanchen.crudproject.dao.ProductDao;
import com.oseanchen.crudproject.model.Product;
import com.oseanchen.crudproject.dto.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Product updateProduct(Integer id, ProductRequest productRequest) {
        Optional<Product> product = getProductById(id);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setProductName(productRequest.getProductName());
            updatedProduct.setUnitPrice(productRequest.getUnitPrice());
            updatedProduct.setUnitsInStock(productRequest.getUnitsInStock());
            updatedProduct.setDiscontinued(productRequest.getDiscontinued());
            updatedProduct.setSupplier(productRequest.getSupplier());
            updatedProduct.setCategory(productRequest.getCategory());
            return productDao.save(updatedProduct);
        }
        return null;
    }

    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setUnitPrice(productRequest.getUnitPrice());
        product.setUnitsInStock(productRequest.getUnitsInStock());
        product.setDiscontinued(productRequest.getDiscontinued());
        product.setSupplier(productRequest.getSupplier());
        product.setCategory(productRequest.getCategory());
        return productDao.save(product);
    }

    public void deleteProductById(Integer id) {
        productDao.deleteById(id);
    }

    public List<Product> searchProducts(String productName) {
        return productDao.findByProductName(productName);
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
}
