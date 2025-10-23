package com.foodstore.foodstore.impl;

import com.foodstore.foodstore.entity.User;
import com.foodstore.foodstore.repository.UserRepository;
import com.foodstore.foodstore.service.AuthService;
import com.foodstore.util.Sha256Util;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        user.setPassword(Sha256Util.hash(user.getPassword()));

        //Solo si no tiene rol asignado, se asigna "cliente"
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("cliente");
        }

        return userRepository.save(user);
    }


    @Override
    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = Sha256Util.hash(password);
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        throw new RuntimeException("Credenciales incorrectas");
    }
}
