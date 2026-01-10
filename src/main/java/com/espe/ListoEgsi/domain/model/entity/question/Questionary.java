package com.espe.ListoEgsi.domain.model.entity.question;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QUESTIONARY")
public class Questionary {

    @Id
    @Size(max = 20)
    @Column(name = "ID_QUESTIONARY", nullable = false, length = 20)
    private String idQuestionary;

    @NotBlank
    @Size(max = 50)
    @Column(name = "QUESTIONARY_NAME", nullable = false)
    private String questionaryName;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotBlank
    @Size(max = 10)
    @Column(name = "PHASE", nullable = false)
    private String phase;

    @OneToMany(mappedBy = "questionary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
