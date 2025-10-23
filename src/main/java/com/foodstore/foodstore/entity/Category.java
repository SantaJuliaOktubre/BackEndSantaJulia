package com.foodstore.foodstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Ejemplo: "Pizzas", "Hamburguesas"
    private String description; // Breve texto opcional
}
