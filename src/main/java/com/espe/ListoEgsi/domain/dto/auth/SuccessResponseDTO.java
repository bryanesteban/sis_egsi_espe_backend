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
@Schema(description = "Respuesta exitosa de operación de cambio")
public class SuccessResponseDTO {
    
    @Schema(description = "Mensaje de éxito", 
            example = "Successfully changing password!")
    private String message;
    
    @Schema(description = "Datos adicionales (opcional)", 
            example = "admin")
    private String username;
}
