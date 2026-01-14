package com.espe.ListoEgsi.domain.dto.phase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de respuestas EGSI.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgsiAnswerDTO {
    
    private String idAnswer;
    private String idProcess;
    private String idQuestion;
    private String idPhase;
    private String answerValue;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
