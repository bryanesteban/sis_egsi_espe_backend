package com.espe.ListoEgsi.domain.dto.phase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para crear una nueva solicitud de aprobación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApprovalRequestDTO {
    
    @NotNull(message = "El ID del proceso es requerido")
    private UUID idProcess;
    
    @NotBlank(message = "El ID de la fase es requerido")
    private String idPhase;
    
    @NotNull(message = "El orden de la fase es requerido")
    private Integer phaseOrder;
    
    @NotBlank(message = "El título de la fase es requerido")
    private String phaseTitle;
    
    @NotBlank(message = "El usuario solicitante es requerido")
    private String requestedBy;
    
    private String comments;
}
