package com.foodstore.foodstore.service;

import com.foodstore.foodstore.entity.User;

public interface AuthService {
    User register(User user);
    User login(String email, String password);
}
