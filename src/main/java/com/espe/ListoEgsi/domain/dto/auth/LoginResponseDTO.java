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
@Schema(description = "Respuesta exitosa del login con token JWT")
public class LoginResponseDTO {
    
    @Schema(description = "Nombre de usuario autenticado", 
            example = "admin")
    private String username;
    
    @Schema(description = "Rol del usuario", 
            example = "ADMIN")
    private String rolename;
    
    @Schema(description = "Token JWT para autenticación", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
