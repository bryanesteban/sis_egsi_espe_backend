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
@Schema(description = "Respuesta de error en autenticación")
public class ErrorResponseDTO {
    
    @Schema(description = "Tipo de error", 
            example = "Credenciales inválidas")
    private String error;
    
    @Schema(description = "Mensaje detallado del error", 
            example = "Usuario no encontrado")
    private String mensaje;
}
