package com.foodstore.foodstore.impl;

import com.foodstore.foodstore.entity.Category;
import com.foodstore.foodstore.entity.Product;
import com.foodstore.foodstore.repository.CategoryRepository;
import com.foodstore.foodstore.repository.ProductRepository;
import com.foodstore.foodstore.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public Product create(Product product) {
        //  Si viene una categoría
        if (product.getCategory() != null) {

            // Caso 1: Si mandan ID, se usa directamente
            if (product.getCategory().getId() != null) {
                Category existing = categoryRepository.findById(product.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                product.setCategory(existing);
            }

            // Caso 2: Si mandan nombre, se busca o se crea
            else if (product.getCategory().getName() != null) {
                String categoryName = product.getCategory().getName();
                var existingCategory = categoryRepository.findByNameIgnoreCase(categoryName);

                if (existingCategory.isPresent()) {
                    product.setCategory(existingCategory.get());
                } else {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    newCategory.setDescription("Categoría creada automáticamente");
                    product.setCategory(categoryRepository.save(newCategory));
                }
            }
        }

        //  Guarda el producto final
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = getById(id);

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setImageUrl(product.getImageUrl());
        existing.setAvailable(product.getAvailable());

        //  Permite actualizar también la categoría
        if (product.getCategory() != null) {
            if (product.getCategory().getId() != null) {
                Category existingCategory = categoryRepository.findById(product.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                existing.setCategory(existingCategory);
            } else if (product.getCategory().getName() != null) {
                String categoryName = product.getCategory().getName();
                var existingCategory = categoryRepository.findByNameIgnoreCase(categoryName);
                if (existingCategory.isPresent()) {
                    existing.setCategory(existingCategory.get());
                } else {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    newCategory.setDescription("Categoría creada automáticamente");
                    existing.setCategory(categoryRepository.save(newCategory));
                }
            }
        }

        return productRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
