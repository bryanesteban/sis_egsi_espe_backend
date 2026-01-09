package com.espe.ListoEgsi.domain.dto.Implantation.phase9;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskTreatmentPlanDTO {
    private UUID idPlanTratamient;
    private UUID idProcess;
    private String institution;
    private String confidentialityLevel;
    private String dateLastModification;
    private String documentResponsible;

}
