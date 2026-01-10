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
@Schema(description = "Respuesta con el nuevo token JWT renovado")
public class RefreshTokenResponseDTO {
    
    @Schema(description = "Nombre de usuario del token", 
            example = "admin")
    private String username;
    
    @Schema(description = "Nuevo token JWT generado", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Mensaje de confirmación", 
            example = "Token renovado exitosamente")
    private String message;
}
