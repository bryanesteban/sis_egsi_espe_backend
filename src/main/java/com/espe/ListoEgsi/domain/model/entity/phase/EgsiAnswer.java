package com.espe.ListoEgsi.domain.model.entity.phase;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una respuesta a una pregunta EGSI.
 * Vincula un proceso específico con una pregunta y almacena la respuesta del usuario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "egsi_answer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID_PROCESS", "ID_QUESTION"})
})
public class EgsiAnswer {

    @Id
    @Column(name = "ID_ANSWER", length = 36)
    private String idAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProcessEgsi process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_QUESTION", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EgsiQuestion question;

    @Column(name = "ID_PHASE", length = 36, nullable = false)
    private String idPhase;

    @Column(name = "ANSWER_VALUE", columnDefinition = "LONGTEXT")
    private String answerValue;

    @Size(max = 20)
    @Column(name = "STATUS")
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (idAnswer == null || idAnswer.isEmpty()) {
            idAnswer = java.util.UUID.randomUUID().toString();
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
