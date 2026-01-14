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
public class EgsiPhaseDTO {
    private String idPhase;
    private String title;
    private String description;
    private Integer order;
    private Boolean isActive;
    private List<EgsiSectionDTO> sections;
}
