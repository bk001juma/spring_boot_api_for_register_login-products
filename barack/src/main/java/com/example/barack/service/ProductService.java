package com.example.barack.service;

import com.example.barack.entity.Product;
import com.example.barack.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file must not be empty.");
        }

        // Check if the file is an image
        String contentType = imageFile.getContentType();
        if (!isImage(contentType)) {
            throw new IllegalArgumentException("File is not a valid image type.");
        }

        // Convert MultipartFile to byte[] and store it in the database
        byte[] imageData = imageFile.getBytes();
        product.setImage(imageData);
        product.setImageType(contentType); // Set the image type

        return productRepository.save(product);
    }


    private boolean isImage(String contentType) {
        return contentType != null && (contentType.startsWith("image/"));
    }

    // Method to get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }


    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
