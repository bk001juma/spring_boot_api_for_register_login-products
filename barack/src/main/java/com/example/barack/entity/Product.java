package com.example.barack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "image", nullable = false)
    @Lob // Indicates that this field can hold large binary data
    private byte[] image;

    @Column(name = "image_type", nullable = false, length = 50) // Field for storing the image type (e.g., "image/jpeg", "image/png")
    private String imageType;

    @Column(name = "category", nullable = false, length = 1500)
    private String category;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
