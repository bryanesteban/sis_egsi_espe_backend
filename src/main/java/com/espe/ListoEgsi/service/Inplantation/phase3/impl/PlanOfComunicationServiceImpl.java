package com.espe.ListoEgsi.service.Inplantation.phase3.impl;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase3.PlanOfComunicationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase3.PlanOfComunication;
import com.espe.ListoEgsi.mapper.PlanOfComunicationMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase3.PlanOfComunicationRepository;
import com.espe.ListoEgsi.service.Inplantation.phase3.PlanOfComunicationService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlanOfComunicationServiceImpl implements PlanOfComunicationService {

    private static final Logger logger = LogManager.getLogger(PlanOfComunicationService.class);

    @Autowired
    private PlanOfComunicationRepository planOfComunicationRepository;

    @Autowired
    private PlanOfComunicationMapper planOfComunicationMapper;

    @Autowired
    private ProcessRepository processRepository;


    @Override
    public List<PlanOfComunicationDTO> findAllPlansOfComunication() {
        List<PlanOfComunication> plansFound = planOfComunicationRepository.findAll();
        return plansFound
                .stream()
                .map(planOfComunicationMapper::toDTO)
                .toList();

    }

    @Override
    public PlanOfComunicationDTO getPlanOfComunication(UUID idPlanOfComunication) {
        logger.info("Fetching Plan of Comunication with ID: {}", idPlanOfComunication);
        return planOfComunicationRepository.findById(idPlanOfComunication)
               .map(planOfComunicationMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Plan of Comunication not found with ID: " + idPlanOfComunication));
    }

    @Override
    public PlanOfComunicationDTO savePlanOfComunication(PlanOfComunicationDTO planOfComunicationSubmit) {
        PlanOfComunication plan = planOfComunicationMapper.toEntity(planOfComunicationSubmit);
        Optional<ProcessEgsi> foundProcess = processRepository.findById(planOfComunicationSubmit.getIdProcess());
                if (!foundProcess.isPresent()) {
                    throw new RuntimeException("Process not found with ID: " + planOfComunicationSubmit.getIdProcess());
                }
                plan.setProcess(foundProcess.get());
        PlanOfComunication savedPlan = planOfComunicationRepository.save(plan);
        return planOfComunicationMapper.toDTO(savedPlan);
                
    }

    @Transactional
    public PlanOfComunicationDTO updatePlanOfComunication(PlanOfComunicationDTO planOfComunicationUpdate) {
      PlanOfComunication planModified = new PlanOfComunication();

      PlanOfComunication existingPlan = planOfComunicationRepository.findById(planOfComunicationUpdate.getIdPlanComunication())
                .orElseThrow(() -> new RuntimeException("Plan of Comunication not found with ID: " + planOfComunicationUpdate.getIdPlanComunication()));

        if(planOfComunicationUpdate.getBackground() != null) existingPlan.setBackground(planOfComunicationUpdate.getBackground());
        if(planOfComunicationUpdate.getObjetiveOfPlan() != null) existingPlan.setObjetiveOfPlan(planOfComunicationUpdate.getObjetiveOfPlan());
        if(planOfComunicationUpdate.getSpecificObjetives() != null) existingPlan.setSpecificObjetives(planOfComunicationUpdate.getSpecificObjetives());
        if(planOfComunicationUpdate.getPlanDescription() != null) existingPlan.setPlanDescription(planOfComunicationUpdate.getPlanDescription());
        if(planOfComunicationUpdate.getScopePlan() != null) existingPlan.setScopePlan(planOfComunicationUpdate.getScopePlan());
        if(planOfComunicationUpdate.getDesingPlan() != null) existingPlan.setDesingPlan(planOfComunicationUpdate.getDesingPlan());
        if(planOfComunicationUpdate.getMaterialsAndChannels() != null) existingPlan.setMaterialsAndChannels(planOfComunicationUpdate.getMaterialsAndChannels());
        if(planOfComunicationUpdate.getStrategiesPlan() != null) existingPlan.setStrategiesPlan(planOfComunicationUpdate.getStrategiesPlan());
        if(planOfComunicationUpdate.getNeedsIdentifications() != null) existingPlan.setNeedsIdentifications(planOfComunicationUpdate.getNeedsIdentifications());
        if(planOfComunicationUpdate.getRolesAndResponsabilities() != null) existingPlan.setRolesAndResponsabilities(planOfComunicationUpdate.getRolesAndResponsabilities());
        if(planOfComunicationUpdate.getResultsAndAchievements() != null) existingPlan.setResultsAndAchievements(planOfComunicationUpdate.getResultsAndAchievements());
        if(planOfComunicationUpdate.getScheduleActivities() != null) existingPlan.setScheduleActivities(planOfComunicationUpdate.getScheduleActivities());
        if(planOfComunicationUpdate.getGlosaryTerms() != null) existingPlan.setGlosaryTerms(planOfComunicationUpdate.getGlosaryTerms());
        if(planOfComunicationUpdate.getDocumentsOfReference() != null) existingPlan.setDocumentsOfReference(planOfComunicationUpdate.getDocumentsOfReference());
        if(planOfComunicationUpdate.getState() != null) existingPlan.setState(planOfComunicationUpdate.getState());

        planModified = planOfComunicationRepository.save(existingPlan);
        log.info("Plan of Comunication with ID{} has been Updated.", planModified.getIdPlanComunication());

        return planOfComunicationMapper.toDTO(planModified);
    }

     @Transactional
    public void deletePlanOfComunication(UUID idPlanOfComunication) {
      logger.info("Deleting Plan of Comunication with ID: {}", idPlanOfComunication);
      PlanOfComunication planToDelete = planOfComunicationRepository.findById(idPlanOfComunication)
                .orElseThrow(() -> new RuntimeException("Plan of Comunication not found with ID: " + idPlanOfComunication));
        planOfComunicationRepository.delete(planToDelete);
        logger.info("Plan of Comunication with ID: {} has been deleted.", idPlanOfComunication);
    }

}
