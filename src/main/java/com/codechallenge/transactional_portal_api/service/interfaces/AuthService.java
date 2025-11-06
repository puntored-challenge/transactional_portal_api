package com.codechallenge.transactional_portal_api.service.interfaces;

import com.codechallenge.transactional_portal_api.model.AuthResponse;
import com.codechallenge.transactional_portal_api.model.User;

public interface AuthService {
    AuthResponse signIn(User user);
    AuthResponse logIn(User user);
    User getUserByUsername(String username);
    User getAuthenticatedUser();
}
