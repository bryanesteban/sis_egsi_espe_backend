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
public class QuestionDTO {

    private UUID idQuestion;

    private String idQuestionary;

    @Size(max = 1000)
    private String description;

    @Size(max = 50)
    private String questionType;

    @Size(max = 1000)
    private String questionJson;
}
