package com.espe.ListoEgsi.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.auth.ChangePasswordDTO;
import com.espe.ListoEgsi.domain.dto.auth.ChangeUsernameDTO;
import com.espe.ListoEgsi.domain.dto.auth.ErrorResponseDTO;
import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.domain.dto.auth.LoginResponseDTO;
import com.espe.ListoEgsi.domain.dto.auth.RefreshTokenRequestDTO;
import com.espe.ListoEgsi.domain.dto.auth.RefreshTokenResponseDTO;
import com.espe.ListoEgsi.domain.dto.auth.SuccessResponseDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.service.auth.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/login")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de credenciales de usuario")
public class loginController {

    @Autowired
    private LoginService loginService;

    @Operation(
        summary = "Autenticar usuario",
        description = "Autentica un usuario y devuelve un token JWT válido por 24 horas. " +
                     "Este endpoint NO requiere autenticación previa. " +
                     "El token obtenido debe ser usado en el header Authorization de las siguientes peticiones."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login exitoso - Token JWT generado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas - Usuario o contraseña incorrectos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Llamamos al servicio que creamos antes
            Map<String, String> response = loginService.authenticate(loginRequest);
            
            // Si todo sale bien, devolvemos el token y el tiempo de 1 hora
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            // Si el usuario o la clave (encriptada) no coinciden
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas", "mensaje", e.getMessage()));
        } catch (Exception e) {
            // Cualquier otro error técnico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor", "mensaje", e.getMessage()));
        }
    }

    @Operation(
        summary = "Cambiar nombre de usuario",
        description = "Permite a un usuario autenticado cambiar su nombre de usuario. " +
                     "Requiere la contraseña actual para confirmar la operación. " +
                     "El nuevo nombre de usuario debe ser diferente al actual."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Nombre de usuario cambiado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos - El nuevo nombre no puede ser igual al anterior o hay errores de validación",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Contraseña incorrecta",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameDTO changeUsernameDTO, 
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
 
        if(changeUsernameDTO.getUsernameOld().equals(changeUsernameDTO.getUsernameNew()))
            throw new RuntimeException("The username can't be the same as before");
        
        try {
            UserDTO changedUsername = loginService.changeUsername(changeUsernameDTO);
            log.info("Successfully changed username: {}", changedUsername.getUsername());
            Map<String,String> response = new HashMap<>();
            response.put("Message","Successfully changed username!");
            response.put("Username",changedUsername.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error changing username: {}", changeUsernameDTO.getUsernameOld(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(
        summary = "Cambiar contraseña",
        description = "Permite a un usuario autenticado cambiar su contraseña. " +
                     "Requiere la contraseña actual para confirmar la operación. " +
                     "La nueva contraseña debe ser diferente a la actual."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Contraseña cambiada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos - La nueva contraseña no puede ser igual a la anterior o hay errores de validación",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Contraseña actual incorrecta",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        if(changePasswordDTO.getPasswordOld().equals(changePasswordDTO.getPasswordNew()))
            throw new RuntimeException("The password can't be the same as before");

        try {
            loginService.changePassword(changePasswordDTO);
            log.info("Successfully changed password: {}", changePasswordDTO.getUsername());
            Map<String,String> response = new HashMap<>();
            response.put("Message","Successfully changed password!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error changing password: {}", changePasswordDTO.getUsername(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(
        summary = "Renovar token JWT",
        description = "Permite renovar un token JWT existente sin necesidad de volver a autenticarse. " +
                     "Útil para mantener la sesión del usuario activa. " +
                     "El token actual debe estar válido (no expirado) para poder ser renovado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Token renovado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RefreshTokenResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Token inválido o expirado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        try {
            Map<String, String> response = loginService.refreshToken(request.getToken());
            log.info("Token refreshed successfully for user: {}", response.get("username"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token inválido o expirado");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
