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
@Schema(description = "Datos para cambiar la contraseña de un usuario")
public class ChangePasswordDTO {
    
    @Schema(description = "Nombre de usuario", 
            example = "admin", 
            required = true)
    private String username;
    
    @Schema(description = "Contraseña actual del usuario", 
            example = "oldPassword123", 
            required = true,
            format = "password")
    private String passwordOld;
    
    @Schema(description = "Nueva contraseña del usuario", 
            example = "newPassword456", 
            required = true,
            format = "password")
    private String passwordNew;
}
