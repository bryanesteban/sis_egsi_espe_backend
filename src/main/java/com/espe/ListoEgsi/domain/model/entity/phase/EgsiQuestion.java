package com.espe.ListoEgsi.domain.model.entity.phase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EGSI_QUESTION")
public class EgsiQuestion {

    @Id
    @Column(name = "ID_QUESTION", length = 36)
    private String idQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SECTION", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EgsiSection section;

    @NotBlank
    @Size(max = 500)
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 20)
    @Column(name = "INPUT_TYPE", nullable = false)
    private String inputType = "TEXTO";

    @Column(name = "IS_REQUIRED", nullable = false)
    private Boolean isRequired = true;

    @Size(max = 500)
    @Column(name = "PLACEHOLDER")
    private String placeholder;

    @Column(name = "MAX_LENGTH")
    private Integer maxLength = 1000;

    @Column(name = "TABLE_CONFIG", columnDefinition = "JSON")
    private String tableConfig;

    @Column(name = "QUESTION_ORDER", nullable = false)
    private Integer questionOrder;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (idQuestion == null || idQuestion.isEmpty()) {
            idQuestion = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
