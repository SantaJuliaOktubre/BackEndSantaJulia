package com.foodstore.foodstore.repository;

import com.foodstore.foodstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Busca una categoría por nombre (sin importar mayúsculas/minúsculas)
    Optional<Category> findByNameIgnoreCase(String name);
}
