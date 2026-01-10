package com.espe.ListoEgsi.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para generación y verificación de hashes BCrypt.
 * Utilidad para generar contraseñas encriptadas para usuarios del sistema.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@RestController
@RequestMapping("/hash")
@Tag(name = "Generador de Hash", description = "API para generar y verificar hashes BCrypt de contraseñas")
public class HashGeneratorController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/generate")
    @Operation(
        summary = "Generar hash BCrypt",
        description = "Genera un hash BCrypt para una contraseña en texto plano. Útil para crear contraseñas de usuarios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hash generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida - falta el campo password")
    })
    public Map<String, String> generateHash(
            @Parameter(description = "Objeto JSON con el campo 'password' conteniendo la contraseña en texto plano", required = true)
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        String hash = passwordEncoder.encode(password);
        
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", String.valueOf(passwordEncoder.matches(password, hash)));
        
        return response;
    }
    
    @PostMapping("/verify")
    @Operation(
        summary = "Verificar hash BCrypt",
        description = "Verifica si una contraseña en texto plano coincide con un hash BCrypt dado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada - revise el campo 'matches' en la respuesta"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida - faltan campos requeridos")
    })
    public Map<String, String> verifyHash(
            @Parameter(description = "Objeto JSON con campos 'password' (texto plano) y 'hash' (BCrypt) a verificar", required = true)
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        String hash = request.get("hash");
        
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", String.valueOf(passwordEncoder.matches(password, hash)));
        
        return response;
    }
}
