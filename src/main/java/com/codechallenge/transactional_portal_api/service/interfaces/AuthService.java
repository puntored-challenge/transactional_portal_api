package com.codechallenge.transactional_portal_api.service.interfaces;

import com.codechallenge.transactional_portal_api.model.AuthResponse;
import com.codechallenge.transactional_portal_api.model.User;

public interface AuthService {
    User getUserByUsername(String username);
    AuthResponse signIn(User user);
    AuthResponse logIn(User user);
}
