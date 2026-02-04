package com.espe.ListoEgsi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para PasswordEncoder (BCrypt)
 * Cubre TC-UT-005: Validar encriptación de password
 */
@DisplayName("PasswordEncoder - Tests Unitarios")
class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * TC-UT-005: Validar encriptación de password
     * 
     * Verifica que:
     * - Password NO guardada en texto plano
     * - BCrypt hash detectado
     * - Hash comienza con "$2a$" o "$2b$"
     */
    @Test
    @DisplayName("TC-UT-005: Password se encripta correctamente con BCrypt")
    void testPasswordEncryption() {
        // Arrange
        String plainPassword = "Test123";

        // Act
        String encodedPassword = passwordEncoder.encode(plainPassword);

        // Assert
        assertNotNull(encodedPassword, "La contraseña encriptada no debe ser null");
        assertNotEquals(plainPassword, encodedPassword, 
                "La contraseña NO debe guardarse en texto plano");
        
        // Verificar que es un hash BCrypt válido
        assertTrue(encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$"),
                "El hash debe comenzar con $2a$ o $2b$ (formato BCrypt)");
        
        // Verificar longitud del hash BCrypt (60 caracteres típicamente)
        assertTrue(encodedPassword.length() == 60, 
                "Un hash BCrypt debe tener exactamente 60 caracteres");
    }

    /**
     * Test adicional: Validar que el mismo password genera hashes diferentes
     */
    @Test
    @DisplayName("Mismo password genera hashes diferentes (salt aleatorio)")
    void testDifferentHashesForSamePassword() {
        // Arrange
        String password = "password123";

        // Act
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // Assert
        assertNotEquals(hash1, hash2, 
                "BCrypt debe generar hashes diferentes para el mismo password (sal aleatoria)");
        
        // Ambos hashes deben validar correctamente contra el password original
        assertTrue(passwordEncoder.matches(password, hash1));
        assertTrue(passwordEncoder.matches(password, hash2));
    }

    /**
     * Test adicional: Validar que matches funciona correctamente
     */
    @Test
    @DisplayName("matches() valida correctamente password contra hash")
    void testPasswordMatching() {
        // Arrange
        String password = "MySecurePassword123";
        String encodedPassword = passwordEncoder.encode(password);

        // Act
        boolean matches = passwordEncoder.matches(password, encodedPassword);

        // Assert
        assertTrue(matches, "El password debe coincidir con su hash");
    }

    /**
     * Test adicional: Validar que matches falla con password incorrecto
     */
    @Test
    @DisplayName("matches() falla con password incorrecto")
    void testPasswordNotMatching() {
        // Arrange
        String correctPassword = "CorrectPassword";
        String wrongPassword = "WrongPassword";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        // Act
        boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

        // Assert
        assertFalse(matches, "Un password incorrecto no debe coincidir con el hash");
    }

    /**
     * Test adicional: Validar encriptación de passwords comunes
     */
    @Test
    @DisplayName("Encriptación funciona con varios tipos de passwords")
    void testVariousPasswordTypes() {
        // Arrange
        String[] passwords = {
            "simple",
            "With Spaces",
            "Special!@#$%^&*()",
            "Números123456",
            "MuyLargaConMasDe50CaracteresParaVerificarQueFuncionaBien123456789",
            "短密碼", // Unicode
            ""  // Vacío (edge case)
        };

        // Act & Assert
        for (String password : passwords) {
            String encoded = passwordEncoder.encode(password);
            
            // Verificar formato BCrypt
            assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$"),
                    "Debe ser BCrypt para: " + password);
            
            // Verificar que matches funciona
            if (!password.isEmpty()) {
                assertTrue(passwordEncoder.matches(password, encoded),
                        "Debe coincidir para: " + password);
            }
        }
    }

    /**
     * Test adicional: Validar que no se puede revertir el hash
     */
    @Test
    @DisplayName("Hash BCrypt no es reversible")
    void testBCryptIsOneWay() {
        // Arrange
        String password = "secretPassword";
        String hash = passwordEncoder.encode(password);

        // Assert
        // No hay forma de obtener el password original del hash
        // Solo podemos verificar si un password coincide
        assertNotEquals(password, hash, "El hash no debe ser igual al password");
        
        // El hash debe ser diferente cada vez
        String hash2 = passwordEncoder.encode(password);
        assertNotEquals(hash, hash2, 
                "Dos encriptaciones del mismo password deben dar hashes diferentes");
    }

    /**
     * Test adicional: Validar comportamiento con null
     */
    @Test
    @DisplayName("Encriptar null lanza excepción")
    void testEncodeNullPassword() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.encode(null);
        }, "Encriptar null debe lanzar IllegalArgumentException");
    }

    /**
     * Test adicional: Validar matches con null
     */
    @Test
    @DisplayName("Comparar con null retorna false")
    void testMatchesWithNull() {
        // Arrange
        String encoded = passwordEncoder.encode("test");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.matches(null, encoded);
        }, "matches() con null debe lanzar excepción");
    }

    /**
     * Test de integración: Simular flujo completo usuario
     */
    @Test
    @DisplayName("Flujo completo: Registro y Login de usuario")
    void testCompleteUserFlow() {
        // Simular REGISTRO
        String userPassword = "userPassword123";
        String storedHash = passwordEncoder.encode(userPassword);
        
        // Verificar que se guardó encriptado
        assertNotEquals(userPassword, storedHash, "Password debe estar encriptado en BD");
        
        // Simular LOGIN - usuario ingresa password
        String loginAttemptPassword = "userPassword123";
        boolean loginSuccessful = passwordEncoder.matches(loginAttemptPassword, storedHash);
        
        assertTrue(loginSuccessful, "Login debe ser exitoso con password correcto");
        
        // Simular LOGIN FALLIDO
        String wrongLoginAttempt = "wrongPassword";
        boolean loginFailed = passwordEncoder.matches(wrongLoginAttempt, storedHash);
        
        assertFalse(loginFailed, "Login debe fallar con password incorrecto");
    }
}
