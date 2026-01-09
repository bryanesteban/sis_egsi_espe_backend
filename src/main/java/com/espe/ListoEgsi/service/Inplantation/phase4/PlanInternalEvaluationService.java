package com.espe.ListoEgsi.service.Inplantation.phase4;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase4.PlanInternalEvaluationDTO;

public interface PlanInternalEvaluationService {

    List<PlanInternalEvaluationDTO> findAllPlansInternalEvaluation();
    PlanInternalEvaluationDTO getPlanInternalEvaluation (UUID idPlanInternalEval);
    PlanInternalEvaluationDTO savePlanInternalEvaluation (PlanInternalEvaluationDTO planInternalEvaluationSubmit);
    PlanInternalEvaluationDTO updatePlanInternalEvaluation (PlanInternalEvaluationDTO planInternalEvaluationUpdate);
    void deletePlanInternalEvaluation (UUID idPlanInternalEval);

}
