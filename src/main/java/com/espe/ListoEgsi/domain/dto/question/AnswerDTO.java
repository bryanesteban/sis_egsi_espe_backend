package com.espe.ListoEgsi.domain.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private UUID idAnswer;
    private Integer idQuestion;
    private UUID idPhase;
    private String answerText;
    private String createdAt;
    private String updatedAt;
    private String answerType;
    private String answerStatus;
}
