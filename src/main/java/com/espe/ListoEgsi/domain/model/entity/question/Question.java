package com.espe.ListoEgsi.domain.model.entity.question;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
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
@Table(name = "QUESTION")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_QUESTION", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_QUESTIONARY", nullable = false)
    private Questionary questionary;

    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 50)
    @Column(name = "QUESTION_TYPE")
    private String questionType;

    @Size(max = 1000)
    @Column(name = "QUESTION_JSON")
    private String questionJson;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}
