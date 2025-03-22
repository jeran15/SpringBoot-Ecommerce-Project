package com.jeran.springbootecommerce.service;

import com.jeran.springbootecommerce.model.Product;
import com.jeran.springbootecommerce.payload.ProductDTO;
import com.jeran.springbootecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts();

    ProductResponse getAllProductsByCategory(Long categoryId);

    ProductResponse getAllProductsByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updatedProductImage(Long productId, MultipartFile image) throws IOException;
}
