package com.espe.ListoEgsi.service.Inplantation.phase9;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase9.RiskTreatmentPlanDTO;


public interface RiskTreatmentPlanService {

    List<RiskTreatmentPlanDTO> findAllRiskTreatmentPlans();
    RiskTreatmentPlanDTO getRiskTreatmentPlan (UUID idRiskTreatmentPlan);
    RiskTreatmentPlanDTO saveRiskTreatmentPlan (RiskTreatmentPlanDTO riskTreatmentPlanSubmitted);
    RiskTreatmentPlanDTO updateRiskTreatmentPlan (RiskTreatmentPlanDTO riskTreatmentPlanUpdate);
    void deleteRiskTreatmentPlan (UUID idRiskTreatmentPlan);
}
