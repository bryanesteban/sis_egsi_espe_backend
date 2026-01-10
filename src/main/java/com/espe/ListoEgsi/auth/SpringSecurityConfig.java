package com.espe.ListoEgsi.auth;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final Logger springSecurityConf = LogManager.getLogger(SpringSecurityConfig.class);

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 2. Autorizar las rutas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST,"/login").permitAll()
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                .requestMatchers("/actuator/**").authenticated()
                .requestMatchers("/users/**").authenticated()
                .anyRequest().authenticated()            
            )
            .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"No autorizado\", \"mensaje\": \"Token JWT no encontrado o inválido\"}");
            })
        )
            // 3. Permitir Basic Auth por si lo necesitas luego
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        springSecurityConf.info("Configuración de seguridad cargada exitosamente.");

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Configure allowed origins from application properties
        if ("*".equals(allowedOrigins)) {
            config.addAllowedOriginPattern("*");
            springSecurityConf.info("Configuring CORS to allow ALL origins!");
        } else {
            String[] origins = allowedOrigins.split(",");
            config.setAllowedOrigins(Arrays.asList(origins));
            springSecurityConf.info("Configuring CORS permissions for origins: " + allowedOrigins);
        }

        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "X-Content-Type-Options",
                "X-Frame-Options",
                "X-XSS-Protection",
                "Pragma",
                "Expires",
                "X-Request-ID",
                "Cache-Control",
                "X-Graph-Token"
        ));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }



}
