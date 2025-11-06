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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * <p>
 * Implementación del servicio de autenticación que gestiona el registro,
 * inicio de sesión y recuperación del usuario autenticado.
 * </p>
 *
 * <p>
 * Este servicio interactúa con la capa de persistencia para validar credenciales,
 * registrar nuevos usuarios y generar tokens JWT seguros.
 * </p>
 *
 * <p>
 * Excepciones personalizadas:
 * <ul>
 *   <li>{@link BadCredentialsException}: cuando las credenciales no coinciden.</li>
 *   <li>{@link UserAlreadyExistsException}: cuando el nombre de usuario ya está registrado.</li>
 * </ul>
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Inicia sesión validando las credenciales del usuario y genera un token JWT.
     *
     * @param user objeto {@link User} con las credenciales ingresadas.
     * @return objeto {@link AuthResponse} que contiene el token JWT.
     * @throws BadCredentialsException si el usuario no existe o la contraseña es incorrecta.
     */
    @Override
    public AuthResponse logIn(User user) {
        User userFound = getUserByUsername(user.getUsername());

        if(!passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            throw new BadCredentialsException();
        }

        return Auth.toAuthResponse(jwtUtil.generateToken(userFound.getUsername()));
    }

    /**
     * Obtiene un usuario desde la base de datos por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar.
     * @return el objeto {@link User} encontrado.
     * @throws BadCredentialsException si el usuario no existe.
     */
    @Override
    public User getUserByUsername(String username) {
        return Auth.toUser(userRepository.findByUsername(username)
                .orElseThrow(BadCredentialsException::new));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <ul>
     *     <li>Valida que el usuario no exista previamente.</li>
     *     <li>Encripta la contraseña antes de guardar.</li>
     *     <li>Devuelve un token JWT para autenticación inmediata.</li>
     * </ul>
     *
     * @param user objeto {@link User} con los datos de registro.
     * @return objeto {@link AuthResponse} con el token JWT generado.
     * @throws UserAlreadyExistsException si el usuario ya está registrado.
     */
    @Override
    public AuthResponse signIn(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(userFound -> {
            throw new UserAlreadyExistsException(user.getUsername());
        });
        UserEntity userEntity = Auth.toUserEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
        return Auth.toAuthResponse(jwtUtil.generateToken(user.getUsername()));
    }

    /**
     * Obtiene el usuario actualmente autenticado en el contexto de seguridad.
     *
     * @return el {@link User} autenticado.
     * @throws RuntimeException si no hay un usuario autenticado.
     */
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }
}
