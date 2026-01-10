package com.espe.ListoEgsi.domain.dto.question;

import java.util.UUID;

import jakarta.validation.constraints.Size;
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

    private UUID idQuestion;

    private UUID idPhase;

    @Size(max = 1000)
    private String answerText;

    @Size(max = 20)
    private String createdAt;

    @Size(max = 20)
    private String updatedAt;

    @Size(max = 20)
    private String answerType;

    @Size(max = 20)
    private String answerStatus;
}
