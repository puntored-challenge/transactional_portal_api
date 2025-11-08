package com.codechallenge.transactional_portal_api.config;

import com.codechallenge.transactional_portal_api.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuración principal de seguridad para la aplicación.
 *
 * <p>Esta clase define la configuración de seguridad basada en JWT para la API.
 * Utiliza un {@link JwtFilter} personalizado para validar los tokens de autenticación
 * y establece las políticas de acceso a los endpoints.</p>
 *
 * <p>Características principales:
 * <ul>
 *   <li>Deshabilita la protección CSRF (ya que se usa autenticación JWT sin sesiones).</li>
 *   <li>Configura el manejo de sesiones como {@link SessionCreationPolicy#STATELESS}.</li>
 *   <li>Permite libre acceso a los endpoints bajo <code>/v1/auth/**</code>.</li>
 *   <li>Requiere autenticación para cualquier otra ruta.</li>
 *   <li>Registra el filtro {@link JwtFilter} antes del {@link UsernamePasswordAuthenticationFilter}.</li>
 * </ul>
 * </p>
 *
 * <p>Esta configuración es necesaria para garantizar que todas las solicitudes sean
 * autenticadas mediante tokens JWT, en lugar de mantener sesiones en el servidor.</p>
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Define el {@link PasswordEncoder} que se utilizará para codificar y verificar contraseñas.
     *
     * <p>Se utiliza {@link BCryptPasswordEncoder} ya que proporciona un algoritmo seguro
     * y adaptable frente a ataques de fuerza bruta.</p>
     *
     * @return un codificador de contraseñas basado en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     *
     * <p>Este método define las reglas de autorización, desactiva CSRF, configura la gestión
     * de sesiones como stateless y agrega el filtro JWT antes del filtro de autenticación
     * estándar de Spring Security.</p>
     *
     * @param http objeto {@link HttpSecurity} usado para construir la configuración.
     * @return la cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error al construir la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expone el {@link AuthenticationManager} como un bean dentro del contexto de Spring.
     *
     * <p>El {@link AuthenticationManager} se utiliza para manejar el proceso de autenticación
     * de usuarios, validando las credenciales proporcionadas contra los detalles almacenados.</p>
     *
     * @param http objeto {@link HttpSecurity} que contiene la configuración de seguridad.
     * @return una instancia del administrador de autenticación.
     * @throws Exception si ocurre un error durante la construcción del {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
