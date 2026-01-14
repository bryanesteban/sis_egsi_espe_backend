package com.espe.ListoEgsi.domain.dto.phase;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EgsiPhasesResponseDTO {
    private Integer totalPhases;
    private Integer activePhases;
    private Integer totalSections;
    private Integer totalQuestions;
    private List<EgsiPhaseDTO> egsiPhases;
}
