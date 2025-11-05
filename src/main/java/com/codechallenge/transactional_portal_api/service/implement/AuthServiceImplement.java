package com.codechallenge.transactional_portal_api.service.implement;


import com.codechallenge.transactional_portal_api.entity.UserEntity;
import com.codechallenge.transactional_portal_api.exception.BadCredentialsException;
import com.codechallenge.transactional_portal_api.exception.UserAlreadyExistsException;
import com.codechallenge.transactional_portal_api.mapper.Auth;
import com.codechallenge.transactional_portal_api.model.AuthResponse;
import com.codechallenge.transactional_portal_api.model.User;
import com.codechallenge.transactional_portal_api.repository.UserRepository;
import com.codechallenge.transactional_portal_api.service.interfaces.AuthService;
import com.codechallenge.transactional_portal_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    public User getUserByUsername(String username) {
        return Auth.toUser(userRepository.findByUsername(username));
    }

    @Override
    public AuthResponse logIn(User user) {
        User userFound = getUserByUsername(user.getUsername());
        if(userFound.getUsername() == null) {
            throw new BadCredentialsException();
        }

        if(userFound.getPassword() != user.getPassword()) {
            throw new BadCredentialsException();
        }

        return Auth.toAuthResponse(jwtUtil.generateToken(userFound.getUsername()));
    }

    @Override
    public AuthResponse signIn(User user) {
        User userFound = getUserByUsername(user.getUsername());
        if(userFound.getUsername() != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        UserEntity userEntity = Auth.toUserEntity(userFound);
        userEntity.setPassword(user.getPassword());
        userRepository.save(userEntity);
        return Auth.toAuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
}
