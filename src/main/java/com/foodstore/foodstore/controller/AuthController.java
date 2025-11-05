package com.foodstore.foodstore.controller;

import com.foodstore.foodstore.entity.User;
import com.foodstore.foodstore.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios (name, email, password)");
        }
        try {
            User newUser = authService.register(user);
            return ResponseEntity.status(201).body(newUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios (email, password)");
        }
        try {
            User loggedUser = authService.login(user.getEmail(), user.getPassword());
            if (loggedUser == null) {
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            }
            return ResponseEntity.ok(loggedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios (name, email, password)");
        }
        try {
            user.setRole("admin");
            User newAdmin = authService.register(user);
            return ResponseEntity.status(201).body(newAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
