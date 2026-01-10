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
@Schema(description = "Datos para cambiar el nombre de usuario")
public class ChangeUsernameDTO {
    
    @Schema(description = "Nombre de usuario actual", 
            example = "adminOld", 
            required = true)
    private String usernameOld;
    
    @Schema(description = "Nuevo nombre de usuario", 
            example = "adminNew", 
            required = true)
    private String usernameNew;
    
    @Schema(description = "Contraseña del usuario para confirmar el cambio", 
            example = "password123", 
            required = true,
            format = "password")
    private String password;

}
