package com.espe.ListoEgsi.domain.model.entity.question;

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
@Table(name = "ANSWERS")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ANSWER", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_QUESTION", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PHASE", nullable = false)
    private Phase phase;

    @Size(max = 1000)
    @Column(name = "ANSWER_TEXT")
    private String answerText;

    @Size(max = 20)
    @Column(name = "CREATED_AT")
    private String createdAt;

    @Size(max = 20)
    @Column(name = "UPDATED_AT")
    private String updatedAt;

    @Size(max = 20)
    @Column(name = "ANSWER_TYPE")
    private String answerType;

    @Size(max = 20)
    @Column(name = "ANSWER_STATUS")
    private String answerStatus;
}
