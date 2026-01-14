package com.espe.ListoEgsi.domain.dto.phase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * DTO para guardar múltiples respuestas de una fase.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveAnswersRequestDTO {
    
    @NotBlank(message = "El ID del proceso es obligatorio")
    private String idProcess;
    
    @NotBlank(message = "El ID de la fase es obligatorio")
    private String idPhase;
    
    private List<AnswerItem> answers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerItem {
        private String idQuestion;
        private String answerValue;
    }
}
