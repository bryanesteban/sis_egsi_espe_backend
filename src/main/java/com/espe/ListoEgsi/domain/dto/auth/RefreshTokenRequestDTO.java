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
@Schema(description = "Solicitud para renovar el token JWT")
public class RefreshTokenRequestDTO {
    
    @Schema(description = "Token JWT actual que se desea renovar", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", 
            required = true)
    private String token;
}
