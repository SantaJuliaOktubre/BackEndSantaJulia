package com.foodstore.foodstore.controller;

import com.foodstore.foodstore.entity.Product;
import com.foodstore.foodstore.repository.UserRepository;
import com.foodstore.foodstore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    //  Obtener  productos (cualquiera puede)
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    //  Obtener producto por id (cualquiera puede)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    //  Crear producto (solo admin)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para crear productos");
        }
        return ResponseEntity.status(201).body(productService.create(product));
    }

    //  Actualizar producto (solo admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para editar productos");
        }
        return ResponseEntity.ok(productService.update(id, product));
    }

    //  Eliminar producto (solo admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("No tenés permiso para eliminar productos");
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  Metodo interno que valida si el email pertenece a un admin
    private boolean isAdmin(String email) {
        var userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && "admin".equalsIgnoreCase(userOpt.get().getRole());
    }
}
