package com.codechallenge.transactional_portal_api.filter;

import com.codechallenge.transactional_portal_api.exception.ResourcesNotFoundException;
import com.codechallenge.transactional_portal_api.exception.UnauthorizedException;
import com.codechallenge.transactional_portal_api.mapper.Auth;
import com.codechallenge.transactional_portal_api.repository.UserRepository;
import com.codechallenge.transactional_portal_api.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Filtro JWT que se ejecuta una sola vez por solicitud para validar el token de autenticación.
 * </p>
 *
 * <p>
 * Este filtro intercepta todas las peticiones HTTP y:
 * <ul>
 *   <li>Extrae el token JWT del encabezado <b>Authorization</b>.</li>
 *   <li>Valida la firma y expiración del token.</li>
 *   <li>Recupera el usuario autenticado desde la base de datos y establece el contexto de seguridad.</li>
 *   <li>Si el token no es válido o el usuario no existe, devuelve un error JSON 401.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Las rutas de Swagger (por ejemplo, <code>/swagger-ui</code> y <code>/v3/api-docs</code>)
 * están excluidas del filtro para permitir el acceso sin autenticación.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Procesa la validación del token JWT antes de permitir el acceso a los endpoints protegidos.
     *
     * @param request     la solicitud HTTP entrante
     * @param response    la respuesta HTTP
     * @param chain       la cadena de filtros para continuar la ejecución
     * @throws ServletException en caso de error del servlet
     * @throws IOException      en caso de error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                Auth.toUser(userRepository.findByUsername(username).orElseThrow(() -> new ResourcesNotFoundException("User not found"))),
                                null,
                                java.util.Collections.emptyList()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            chain.doFilter(request, response);
        } catch (UnauthorizedException | ResourcesNotFoundException ex) {
            sendErrorResponse(response, ex.getMessage());
        }
    }

    /**
     * Envía una respuesta JSON estándar en caso de error de autenticación.
     *
     * @param response la respuesta HTTP donde se escribirá el error
     * @param message  el mensaje de error personalizado
     * @throws IOException si ocurre un error al escribir la respuesta
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> error = new HashMap<>();
        error.put("error", "UNAUTHORIZED");
        error.put("message", message);

        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}