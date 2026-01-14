package com.espe.ListoEgsi.domain.model.entity.phase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EGSI_PHASE")
public class EgsiPhase {

    @Id
    @Column(name = "ID_PHASE", length = 36)
    private String idPhase;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PHASE_ORDER", nullable = false)
    private Integer phaseOrder;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = true;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sectionOrder ASC")
    @BatchSize(size = 20)
    @Builder.Default
    private Set<EgsiSection> sections = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (idPhase == null || idPhase.isEmpty()) {
            idPhase = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
