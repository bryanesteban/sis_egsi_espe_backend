package com.espe.ListoEgsi.service.Inplantation.phase3;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase3.PlanOfComunicationDTO;


public interface PlanOfComunicationService {

    List<PlanOfComunicationDTO> findAllPlansOfComunication();
    PlanOfComunicationDTO getPlanOfComunication (UUID idPlanOfComunication);
    PlanOfComunicationDTO savePlanOfComunication (PlanOfComunicationDTO planOfComunicationSubmit);
    PlanOfComunicationDTO updatePlanOfComunication (PlanOfComunicationDTO planOfComunicationUpdate);
    void deletePlanOfComunication (UUID idPlanOfComunication);

}
