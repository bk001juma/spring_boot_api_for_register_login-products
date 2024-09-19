package com.example.barack.service;

import com.example.barack.entity.Product;
import com.example.barack.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Value("${image.storage.path}")
    private String imageStoragePath;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        String imageFileName = imageFile.getOriginalFilename();
        String imagePath = Paths.get(imageStoragePath, imageFileName).toString();

        try {
            imageFile.transferTo(new File(imagePath));
        } catch (IOException e) {
            logger.error("Failed to save image file: " + e.getMessage(), e);
            throw e;
        }

        // Use a public URL for the image that can be accessed by the frontend
        String imageUrl = "/images/" + imageFileName;  // This is the URL to access the image
        product.setImage(imageUrl);

        return productRepository.save(product);
    }
    // Method to get products by category
    public List<Product> getProductsByCategory(String category) {
        List<Product> allProducts = getAllProducts(); // Assuming you have this method already
        return allProducts.stream()
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
