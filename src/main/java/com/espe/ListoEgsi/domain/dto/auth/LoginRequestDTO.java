package com.espe.ListoEgsi.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de autenticación para el login")
public class LoginRequestDTO {
    
    @Schema(description = "Nombre de usuario", 
            example = "admin", 
            required = true)
    private String username;
    
    @Schema(description = "Contraseña del usuario", 
            example = "password123", 
            required = true,
            format = "password")
    private String password;
}
