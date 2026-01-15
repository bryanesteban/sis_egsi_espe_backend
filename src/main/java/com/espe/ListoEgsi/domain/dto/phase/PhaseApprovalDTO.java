package com.espe.ListoEgsi.domain.dto.phase;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar una solicitud de aprobación de fase.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseApprovalDTO {
    
    private UUID idApproval;
    private UUID idProcess;
    private String processName;
    private String idPhase;
    private Integer phaseOrder;
    private String phaseTitle;
    private String status;
    private String requestedBy;
    private LocalDateTime requestedAt;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String comments;
    private String rejectionReason;
}
