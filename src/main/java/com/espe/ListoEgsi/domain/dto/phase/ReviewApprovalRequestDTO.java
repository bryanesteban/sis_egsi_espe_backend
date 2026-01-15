package com.espe.ListoEgsi.domain.dto.phase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para revisar (aprobar/rechazar) una solicitud.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewApprovalRequestDTO {
    
    @NotNull(message = "El ID de la aprobación es requerido")
    private UUID idApproval;
    
    @NotBlank(message = "La acción es requerida (APPROVED/REJECTED)")
    private String action; // APPROVED o REJECTED
    
    @NotBlank(message = "El revisor es requerido")
    private String reviewedBy;
    
    private String rejectionReason; // Solo requerido si action = REJECTED
}
