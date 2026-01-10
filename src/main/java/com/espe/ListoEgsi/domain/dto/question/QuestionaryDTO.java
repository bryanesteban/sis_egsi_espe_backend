package com.espe.ListoEgsi.domain.dto.question;

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
public class QuestionaryDTO {

    @Size(max = 36)
    private String idQuestionary;

    @NotBlank
    @Size(max = 50)
    private String questionaryName;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    @Size(max = 10)
    private String phase;
}
