package com.espe.ListoEgsi.domain.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Integer idQuestion;
    private String idQuestionary;
    private String description;
    private String questionType;
    private String questionJson;
}
