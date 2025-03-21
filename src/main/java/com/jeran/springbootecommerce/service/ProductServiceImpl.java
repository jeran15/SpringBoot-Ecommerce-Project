package com.jeran.springbootecommerce.service;

import com.jeran.springbootecommerce.exceptions.ResourceNotFoundException;
import com.jeran.springbootecommerce.model.Category;
import com.jeran.springbootecommerce.model.Product;
import com.jeran.springbootecommerce.payload.ProductDTO;
import com.jeran.springbootecommerce.payload.ProductResponse;
import com.jeran.springbootecommerce.repository.CategoryRepository;
import com.jeran.springbootecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01 * product.getPrice()));
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products =  productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;
    }
}
