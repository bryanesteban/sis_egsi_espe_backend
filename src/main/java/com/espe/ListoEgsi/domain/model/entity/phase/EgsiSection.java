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
@Table(name = "EGSI_SECTION")
public class EgsiSection {

    @Id
    @Column(name = "ID_SECTION", length = 36)
    private String idSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PHASE", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EgsiPhase phase;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SECTION_ORDER", nullable = false)
    private Integer sectionOrder;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("questionOrder ASC")
    @BatchSize(size = 50)
    @Builder.Default
    private Set<EgsiQuestion> questions = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (idSection == null || idSection.isEmpty()) {
            idSection = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
