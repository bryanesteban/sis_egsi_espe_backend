package com.espe.ListoEgsi.domain.dto.Implantation.phase7;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEvaluationOfTreatmentRiskDTO {

    private UUID idReportTreatment;
    private UUID idProcess;
    private String objetive;
    private String description;
    private String contextEvaluation;
    private String methodologyEvaluation;
    private String identificationValorationAssets;
    private String identificationRiskAndVulnerabilities;
    private String identificationControl;
    private String evaluationRisk;
    private String treatmentRisk;
    private String nivelRisk;
    private String clarificationNote;
}
