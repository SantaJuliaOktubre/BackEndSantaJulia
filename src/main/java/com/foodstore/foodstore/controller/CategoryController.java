package com.foodstore.foodstore.controller;

import com.foodstore.foodstore.entity.Category;
import com.foodstore.foodstore.repository.UserRepository;
import com.foodstore.foodstore.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    //  Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    //  Obtener una categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    //  Crear categoría (solo admin)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para crear categorías");
        }
        return ResponseEntity.status(201).body(categoryService.create(category));
    }

    //  Actualizar categoría (solo admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category category, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para editar categorías");
        }
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    //  Eliminar categoría (solo admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para eliminar categorías");
        }
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  Valida si el usuario es admin
    private boolean isAdmin(String email) {
        var userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && "admin".equalsIgnoreCase(userOpt.get().getRole());
    }
}
