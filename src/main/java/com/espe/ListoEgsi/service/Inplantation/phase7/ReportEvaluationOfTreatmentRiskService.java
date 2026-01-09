package com.espe.ListoEgsi.service.Inplantation.phase7;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase7.ReportEvaluationOfTreatmentRiskDTO;

public interface ReportEvaluationOfTreatmentRiskService {

    List<ReportEvaluationOfTreatmentRiskDTO> findAllReportEvaluationOfTreatmentRisks();
    ReportEvaluationOfTreatmentRiskDTO getReportEvaluationOfTreatmentRisk (UUID idReportEvaluationRisk);
    ReportEvaluationOfTreatmentRiskDTO saveReportEvaluationOfTreatmentRisk (ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskSubmit);
    ReportEvaluationOfTreatmentRiskDTO updateReportEvaluationOfTreatmentRisk (ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskUpdate);
    void deleteReportEvaluationOfTreatmentRisk (UUID idReportEvaluationRisk);

}
