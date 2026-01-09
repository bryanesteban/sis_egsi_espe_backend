package com.espe.ListoEgsi.service.Inplantation.phase6;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase6.MethodologyEvaluationDTO;

public interface MethodologyEvaluationService {

    List<MethodologyEvaluationDTO> findAllMethodologyEvaluations();
    MethodologyEvaluationDTO getMethodologyEvaluation (UUID idMethodologyEval);
    MethodologyEvaluationDTO saveMethodologyEvaluation (MethodologyEvaluationDTO methodologyEvaluationSubmit);
    MethodologyEvaluationDTO updateMethodologyEvaluation (MethodologyEvaluationDTO methodologyEvaluationUpdate);
    void deleteMethodologyEvaluation (UUID idMethodologyEval);
}
