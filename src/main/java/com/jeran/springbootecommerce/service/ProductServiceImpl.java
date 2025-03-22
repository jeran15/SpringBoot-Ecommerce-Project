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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
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

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO, Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01 * product.getPrice()));
        productFromDB.setSpecialPrice(specialPrice);


        Product updateProduct = productRepository.save(productFromDB);
        return modelMapper.map(updateProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product deleteProductFormDB = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(deleteProductFormDB);

        return modelMapper.map(deleteProductFormDB,ProductDTO.class);
    }

    @Override
    public ProductDTO updatedProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        String path = "images/";
        String fileName = uploadImage(path,image);

        productFromDB.setImage(fileName);

        Product updateProductImage = productRepository.save(productFromDB);
        return modelMapper.map(updateProductImage,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //File name of Original
        String originalFileName = file.getOriginalFilename();

        //Generate unique filename
        String randomId = UUID.randomUUID().toString();

        //abc.jpg --> 1234 ---> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        //check if the path exist and create
        File foler = new File(path);
        if(!foler.exists())
            foler.mkdirs();

        //Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        //returning file name
        return fileName;
    }


}
