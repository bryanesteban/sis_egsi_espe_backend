package com.espe.ListoEgsi.domain.model.entity.question;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para gestionar las solicitudes de aprobación de fases.
 * Cada fase completada debe pasar por un proceso de aprobación antes
 * de poder avanzar a la siguiente fase.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PHASE_APPROVAL_REQUEST")
public class PhaseApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_APPROVAL", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idApproval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false)
    private ProcessEgsi process;

    @Column(name = "ID_PHASE", nullable = false)
    private String idPhase; // ID de la fase EGSI estándar

    @Column(name = "PHASE_ORDER", nullable = false)
    private Integer phaseOrder;

    @Column(name = "PHASE_TITLE", nullable = false)
    private String phaseTitle;

    /**
     * Estado de la solicitud:
     * PENDING - Esperando revisión
     * APPROVED - Aprobada, puede avanzar
     * REJECTED - Rechazada, debe corregir
     * CANCELLED - Cancelada por el usuario
     */
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "REQUESTED_BY", nullable = false)
    private String requestedBy;

    @Column(name = "REQUESTED_AT", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "REVIEWED_BY")
    private String reviewedBy;

    @Column(name = "REVIEWED_AT")
    private LocalDateTime reviewedAt;

    @Column(name = "COMMENTS", length = 2000)
    private String comments;

    @Column(name = "REJECTION_REASON", length = 2000)
    private String rejectionReason;

    @PrePersist
    public void prePersist() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }
}
