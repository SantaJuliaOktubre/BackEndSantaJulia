package com.foodstore.foodstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private Boolean available = true;

    //  Relación con categoría
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
