package com.example.barack.controller;

import com.example.barack.entity.Product;
import com.example.barack.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setDescription(description);
            product.setPrice(price);

            // Save the product using ProductService
            Product savedProduct = productService.saveProduct(product, imageFile);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedProduct.getId());
            response.put("name", savedProduct.getName());
            response.put("category", savedProduct.getCategory());
            response.put("description", savedProduct.getDescription());
            response.put("price", savedProduct.getPrice());
            response.put("image", "Image stored in database"); // Indicating that the image is stored in DB

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to save product due to a file handling issue. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred while saving the product. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable int id) {
        return productService.getProductById(id)
                .map(product -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", product.getId());
                    response.put("name", product.getName());
                    response.put("category", product.getCategory());
                    response.put("description", product.getDescription());
                    response.put("price", product.getPrice());
                    response.put("image", "Image data stored in database"); // Indicating that the image is stored in DB
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Product with ID " + id + " not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping("get")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("category")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list if no products found
        }
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable int id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product with ID " + id + " has been successfully deleted.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Product with ID " + id + " not found. No product was deleted.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @RestController
    public class DebugController {
        @GetMapping("/test")
        public String test() {
            return "Server is running!";
        }
    }
}
