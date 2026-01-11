package com.espe.ListoEgsi.domain.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionaryDTO {

    private String idQuestionary;
    private String questionaryName;
    private String description;
    private String phase;
}
