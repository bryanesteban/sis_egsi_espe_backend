package com.espe.ListoEgsi.service.Inplantation.phase4.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase4.PlanInternalEvaluationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase4.PlanInternalEvaluation;
import com.espe.ListoEgsi.mapper.PlanInternalEvaluationMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase4.PlanInternalEvaluationRepository;
import com.espe.ListoEgsi.service.Inplantation.phase4.PlanInternalEvaluationService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlanInternalEvaluationServiceImpl implements PlanInternalEvaluationService{
   
    @Autowired
    private PlanInternalEvaluationRepository planInternalEvaluationRepository;

    @Autowired
    private PlanInternalEvaluationMapper planInternalEvaluationMapper;

    @Autowired
    private ProcessRepository processRepository;
   

   
    public List<PlanInternalEvaluationDTO> findAllPlansInternalEvaluation() {
        List<PlanInternalEvaluation> plansFound = planInternalEvaluationRepository.findAll();
        return plansFound
                .stream()
                .map(planInternalEvaluationMapper::toDTO)
                .toList();

    }

    @Override
    public PlanInternalEvaluationDTO getPlanInternalEvaluation(UUID idPlanInternalEval) {
        log.info("Fetching Plan of Internal Evaluation with ID: {}", idPlanInternalEval);
        return planInternalEvaluationRepository.findById(idPlanInternalEval)
               .map(planInternalEvaluationMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Plan of Internal Evaluation not found with ID: " + idPlanInternalEval));
    }

    @Override
    public PlanInternalEvaluationDTO savePlanInternalEvaluation(
            PlanInternalEvaluationDTO planInternalEvaluationSubmit) {
        PlanInternalEvaluation PlanInternalFound = planInternalEvaluationMapper.toEntity(planInternalEvaluationSubmit);
        Optional<ProcessEgsi> processFound = processRepository.findById(planInternalEvaluationSubmit.getIdProcess());
        if(!processFound.isPresent()){
            throw new RuntimeException("Process not found with ID: " + planInternalEvaluationSubmit.getIdProcess());
        }
        PlanInternalFound.setProcess(processFound.get());
        PlanInternalEvaluation planSaved = planInternalEvaluationRepository.save(PlanInternalFound);
        return planInternalEvaluationMapper.toDTO(planSaved);

   }

   @Transactional
    public PlanInternalEvaluationDTO updatePlanInternalEvaluation(
            PlanInternalEvaluationDTO planInternalEvaluationUpdate) {
        PlanInternalEvaluation planModified = new PlanInternalEvaluation();

        PlanInternalEvaluation existingPlan = planInternalEvaluationRepository.findById(planInternalEvaluationUpdate.getIdPlanInternalEval())
                .orElseThrow(() -> new RuntimeException("Plan of Internal Evaluation not found with ID: " + planInternalEvaluationUpdate.getIdPlanInternalEval()));
        
        if(planInternalEvaluationUpdate.getBackground() != null) existingPlan.setBackground(planInternalEvaluationUpdate.getBackground());
        if(planInternalEvaluationUpdate.getDocumentObjetive() != null) existingPlan.setDocumentObjetive(planInternalEvaluationUpdate.getDocumentObjetive());
        if(planInternalEvaluationUpdate.getScopePlan() != null) existingPlan.setScopePlan(planInternalEvaluationUpdate.getScopePlan());
        if(planInternalEvaluationUpdate.getCriterionPlan() != null) existingPlan.setCriterionPlan(planInternalEvaluationUpdate.getCriterionPlan());
        if(planInternalEvaluationUpdate.getMethodologyPlan() != null) existingPlan.setMethodologyPlan(planInternalEvaluationUpdate.getMethodologyPlan());
        if(planInternalEvaluationUpdate.getCronogramPlan() != null) existingPlan.setCronogramPlan(planInternalEvaluationUpdate.getCronogramPlan());
        if(planInternalEvaluationUpdate.getResourcePlan() != null) existingPlan.setResourcePlan(planInternalEvaluationUpdate.getResourcePlan());
        if(planInternalEvaluationUpdate.getDocumentationFindingsPlan() != null) existingPlan.setDocumentationFindingsPlan(planInternalEvaluationUpdate.getDocumentationFindingsPlan());
        if(planInternalEvaluationUpdate.getDocumentationFindingsPlanTable() != null) existingPlan.setDocumentationFindingsPlanTable(planInternalEvaluationUpdate.getDocumentationFindingsPlanTable());
        if(planInternalEvaluationUpdate.getResultsPlan() != null) existingPlan.setResultsPlan(planInternalEvaluationUpdate.getResultsPlan());
        if(planInternalEvaluationUpdate.getGlosaryTermsTable() != null) existingPlan.setGlosaryTermsTable(planInternalEvaluationUpdate.getGlosaryTermsTable());
        if(planInternalEvaluationUpdate.getDocumentsReference() != null) existingPlan.setDocumentsReference(planInternalEvaluationUpdate.getDocumentsReference());
        if(planInternalEvaluationUpdate.getControlVersion() != null) existingPlan.setControlVersion(planInternalEvaluationUpdate.getControlVersion());
        if(planInternalEvaluationUpdate.getChangeHistorial() != null) existingPlan.setChangeHistorial(planInternalEvaluationUpdate.getChangeHistorial());
        if(planInternalEvaluationUpdate.getScheduleWorking() != null) existingPlan.setScheduleWorking(planInternalEvaluationUpdate.getScheduleWorking());

        planModified = planInternalEvaluationRepository.save(existingPlan);
        log.info("Plan of Internal Evaluation with ID: {} has been updated.", planInternalEvaluationUpdate.getIdPlanInternalEval());

        return planInternalEvaluationMapper.toDTO(planModified);


            }

    @Transactional
    public void deletePlanInternalEvaluation(UUID idPlanInternalEval) {
    
        log.info("Deleting Plan of Internal Evaluation with ID: {}", idPlanInternalEval);
        PlanInternalEvaluation planToDelete = planInternalEvaluationRepository.findById(idPlanInternalEval)
                    .orElseThrow(() -> new RuntimeException("Plan of Internal Evaluation not found with ID: " + idPlanInternalEval));
            planInternalEvaluationRepository.delete(planToDelete);
            log.info("Plan of Internal Evaluation with ID: {} has been deleted.", idPlanInternalEval);

    }


}
