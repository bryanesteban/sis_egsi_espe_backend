package com.espe.ListoEgsi.service.auth;

import com.espe.ListoEgsi.auth.JwtUtil;
import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.domain.model.entity.setting.User;
import com.espe.ListoEgsi.repository.UserRepository;
import com.espe.ListoEgsi.service.auth.impl.LoginServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para LoginService
 * Cubre TC-UT-001, TC-UT-002, TC-UT-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService - Tests Unitarios")
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImp loginService;

    private User validUser;
    private LoginRequestDTO validLoginRequest;

    @BeforeEach
    void setUp() {
        // Preparar usuario válido
        validUser = new User();
        validUser.setUsername("mdavalos");
        validUser.setPassword("$2a$10$encodedPassword"); // Simulando BCrypt
        validUser.setRol("ADMIN");

        // Preparar request válido
        validLoginRequest = LoginRequestDTO.builder()
                .username("mdavalos")
                .password("password")
                .build();
    }

    /**
     * TC-UT-001: Validar login con credenciales correctas
     * 
     * Verifica que:
     * - Login exitoso con credenciales correctas
     * - Token JWT generado
     * - Sin errores
     */
    @Test
    @DisplayName("TC-UT-001: Login exitoso con credenciales correctas")
    void testLoginWithValidCredentials() {
        // Arrange
        when(userRepository.findByUsername("mdavalos")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password", validUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken("mdavalos")).thenReturn("mock-jwt-token-123456");

        // Act
        Map<String, String> response = loginService.authenticate(validLoginRequest);

        // Assert
        assertNotNull(response, "La respuesta no debe ser null");
        assertEquals("mdavalos", response.get("username"), "El username debe coincidir");
        assertEquals("ADMIN", response.get("rolename"), "El rol debe coincidir");
        assertEquals("mock-jwt-token-123456", response.get("token"), "El token debe estar presente");

        // Verificar que se llamaron los métodos correctos
        verify(userRepository, times(1)).findByUsername("mdavalos");
        verify(passwordEncoder, times(1)).matches("password", validUser.getPassword());
        verify(jwtUtil, times(1)).generateToken("mdavalos");
    }

    /**
     * TC-UT-002: Validar login con password incorrecta
     * 
     * Verifica que:
     * - Login falla con password incorrecta
     * - Excepción RuntimeException lanzada
     * - Sin token generado
     */
    @Test
    @DisplayName("TC-UT-002: Login falla con password incorrecta")
    void testLoginWithInvalidPassword() {
        // Arrange
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .username("mdavalos")
                .password("wrongPassword")
                .build();

        when(userRepository.findByUsername("mdavalos")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("wrongPassword", validUser.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginService.authenticate(invalidRequest);
        }, "Debe lanzar RuntimeException para password incorrecta");

        assertEquals("Clave incorrecta", exception.getMessage(), 
                "El mensaje de error debe ser 'Clave incorrecta'");

        // Verificar que NO se generó token
        verify(jwtUtil, never()).generateToken(anyString());
    }

    /**
     * TC-UT-003: Validar login con usuario inexistente
     * 
     * Verifica que:
     * - Login falla con usuario que no existe
     * - Excepción RuntimeException lanzada
     * - Mensaje claro de error
     */
    @Test
    @DisplayName("TC-UT-003: Login falla con usuario inexistente")
    void testLoginWithNonExistentUser() {
        // Arrange
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .username("usuarioInexistente")
                .password("anyPassword")
                .build();

        when(userRepository.findByUsername("usuarioInexistente")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginService.authenticate(invalidRequest);
        }, "Debe lanzar RuntimeException para usuario inexistente");

        assertEquals("Usuario no encontrado", exception.getMessage(), 
                "El mensaje de error debe ser 'Usuario no encontrado'");

        // Verificar que NO se intentó validar password ni generar token
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    /**
     * Test adicional: Validar que el login funciona con diferentes roles
     */
    @Test
    @DisplayName("Login exitoso con rol APROBADOR")
    void testLoginWithApproverRole() {
        // Arrange
        validUser.setRol("APROBADOR");
        when(userRepository.findByUsername("mdavalos")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password", validUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken("mdavalos")).thenReturn("token-aprobador");

        // Act
        Map<String, String> response = loginService.authenticate(validLoginRequest);

        // Assert
        assertEquals("APROBADOR", response.get("rolename"), "El rol debe ser APROBADOR");
        assertNotNull(response.get("token"), "Debe generar token para aprobador");
    }

    /**
     * Test adicional: Validar que el login funciona con rol IMPLEMENTADOR
     */
    @Test
    @DisplayName("Login exitoso con rol IMPLEMENTADOR")
    void testLoginWithImplementerRole() {
        // Arrange
        validUser.setRol("IMPLEMENTADOR");
        when(userRepository.findByUsername("mdavalos")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password", validUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken("mdavalos")).thenReturn("token-implementador");

        // Act
        Map<String, String> response = loginService.authenticate(validLoginRequest);

        // Assert
        assertEquals("IMPLEMENTADOR", response.get("rolename"), "El rol debe ser IMPLEMENTADOR");
        assertNotNull(response.get("token"), "Debe generar token para implementador");
    }
}
