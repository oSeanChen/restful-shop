package com.oseanchen.restfulshop.service;

import com.oseanchen.restfulshop.dao.ProductDao;
import com.oseanchen.restfulshop.dto.ProductRequest;
import com.oseanchen.restfulshop.model.Product;
import com.oseanchen.restfulshop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct1;
    private Product mockProduct2;
    private ProductRequest mockProductRequest;
    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        mockProduct1 = new Product();
        mockProduct1.setId(1);
        mockProduct1.setProductName("Test Product");
        mockProduct1.setUnitPrice(10.0);
        mockProduct1.setUnitsInStock(100);
        mockProduct1.setDiscontinued(false);
        Supplier supplier1 = new Supplier();
        supplier1.setId(1);
        mockProduct1.setSupplier(supplier1);

        mockProduct2 = new Product();
        mockProduct2.setId(2);
        mockProduct2.setProductName("Test Wireless Mouse");
        mockProduct2.setDiscontinued(false);
        Supplier supplier2 = new Supplier();
        supplier2.setId(2);
        mockProduct2.setSupplier(supplier2);
        mockProduct2.setUnitPrice(24.99);
        mockProduct2.setUnitsInStock(103);

        mockProductRequest = new ProductRequest();
        mockProductRequest.setProductName("New/Update Product");
        mockProductRequest.setUnitPrice(15.0);
        mockProductRequest.setUnitsInStock(50);
        mockProductRequest.setDiscontinued(false);
        mockProductRequest.setSupplier(supplier1);
    }

    @Test
    public void testGetAllProducts() {
        mockProducts = Arrays.asList(mockProduct1, mockProduct2);
        when(productDao.findAll()).thenReturn(mockProducts);
        List<Product> products = productService.getAllProducts();
        assertEquals(products.get(0).getProductName(), "Test Product");
        assertEquals(products.get(1).getProductName(), "Test Wireless Mouse");
        assertTrue(products.size() == 2);
        verify(productDao, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        when(productDao.findById(1)).thenReturn(Optional.of(mockProduct1));

        Optional<Product> product = productService.getProductById(1);

        assertTrue(product.isPresent());
        assertEquals(product.get().getProductName(), "Test Product");
        assertEquals(product.get().getUnitPrice(), 10.0);
        assertEquals(product.get().getUnitsInStock(), 100);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productDao.findById(3)).thenReturn(Optional.empty());

        Optional<Product> product = productService.getProductById(3);

        assertFalse(product.isPresent());
        verify(productDao, times(1)).findById(3);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = productService.convertToModel(mockProductRequest);

        when(productDao.save(any(Product.class))).thenReturn(newProduct);

        Product createdProduct = productService.createProduct(mockProductRequest);

        assertNotNull(createdProduct);
        assertEquals("New/Update Product", createdProduct.getProductName());
        verify(productDao, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        Product updateMcokProduct = productService.convertToModel(mockProductRequest);

        when(productDao.findById(1)).thenReturn(Optional.of(mockProduct1));
        when(productDao.save(any(Product.class))).thenReturn(updateMcokProduct);

        Product updatedProduct = productService.updateProduct(1, mockProductRequest);

        assertNotNull(updatedProduct);
        assertEquals("New/Update Product", updatedProduct.getProductName());
        verify(productDao, times(1)).findById(1);
        verify(productDao, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productDao.findById(3)).thenReturn(Optional.empty());
        Product updatedProduct = productService.updateProduct(3, mockProductRequest);
        assertNull(updatedProduct);
        verify(productDao, times(1)).findById(3);
        verify(productDao, never()).save(any(Product.class));

    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productDao).deleteById(1);
        productService.deleteProductById(1);
        verify(productDao, times(1)).deleteById(1);
    }


    @Test
    void testSearchAndSortProducts() {
        mockProducts = Arrays.asList(mockProduct2, mockProduct1);
        Page<Product> productPage = new PageImpl<>(mockProducts);

        when(productDao.findByProductNameContainingIgnoreCase(eq("Test"), any(PageRequest.class)))
                .thenReturn(productPage);

        List<Product> result = productService.searchAndSortProducts("Test", "id", "desc", 0, 10);

        assertEquals(2, result.size());
        assertEquals(mockProduct2, result.get(0));
        assertEquals(mockProduct1, result.get(1));
        verify(productDao).findByProductNameContainingIgnoreCase(eq("Test"), any(PageRequest.class));
    }

    @Test
    void testSearchAndSortProducts_NullOrEmptyProductName() {
        mockProducts = Arrays.asList(mockProduct1, mockProduct2);
        Page<Product> productPage = new PageImpl<>(mockProducts);
        when(productDao.findAll(any(PageRequest.class))).thenReturn(productPage);

        // null product name
        List<Product> resultNull = productService.searchAndSortProducts(null, "id", "asc", 0, 10);
        assertEquals(2, resultNull.size());
        assertEquals(mockProduct1, resultNull.get(0));

        // empty product name
        List<Product> resultEmpty = productService.searchAndSortProducts("", "id", "asc", 0, 10);
        assertEquals(2, resultEmpty.size());
        assertEquals(mockProduct1, resultEmpty.get(0));

        verify(productDao, times(2)).findAll(any(PageRequest.class));
    }
}