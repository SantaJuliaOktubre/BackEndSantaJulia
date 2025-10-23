package com.foodstore.foodstore.repository;

import com.foodstore.foodstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
