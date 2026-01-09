package com.espe.ListoEgsi.domain.dto.Implantation.phase6;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodologyEvaluationDTO {

    private UUID idMethodologyEval;
    private UUID idProcess;
    private String objetive;
    private String scope;
    private String users;
    private String processMethodology;
    private String assetsMethodology;
    private String identificationMethodology;
    private String impactProbability;
    private String acceptanceCriteriaMethodology;
    private String riskTreatmentMethodology;
    private String revisionPeriodMethodology;
    private String declarationApplycabilityMethodology;
    private String reportsMethodology;
    private String validityManagementMethodology;
    private String notesMethodology;

}
