package com.espe.ListoEgsi.service.Inplantation.phase9.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase9.RiskTreatmentPlanDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase9.RiskTreatmentPlan;
import com.espe.ListoEgsi.mapper.RiskTreatmentPlanMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase9.RiskTreatmentPlanRepository;
import com.espe.ListoEgsi.service.Inplantation.phase9.RiskTreatmentPlanService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RiskTreatmentPlanServiceImpl implements RiskTreatmentPlanService {

    @Autowired
    private RiskTreatmentPlanRepository riskTreatmentPlanRepository;

    @Autowired
    private RiskTreatmentPlanMapper riskTreatmentPlanMapper;

    @Autowired
    private ProcessRepository processRepository;

    @Override
    public List<RiskTreatmentPlanDTO> findAllRiskTreatmentPlans() {
        List<RiskTreatmentPlan> riskTreatmentPlans = riskTreatmentPlanRepository.findAll();
        return riskTreatmentPlans
                .stream()
                .map(riskTreatmentPlanMapper::toDTO)
                .toList(); 
    }

    @Override
    public RiskTreatmentPlanDTO getRiskTreatmentPlan(UUID idRiskTreatmentPlan) {
        log.info("Fetching Risk Treatment Plan with ID: {}", idRiskTreatmentPlan);
        return riskTreatmentPlanRepository.findById(idRiskTreatmentPlan)
               .map(riskTreatmentPlanMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Risk Treatment Plan not found with ID: " + idRiskTreatmentPlan));
    }

    @Override
    public RiskTreatmentPlanDTO saveRiskTreatmentPlan(RiskTreatmentPlanDTO riskTreatmentPlanSubmitted) {
         RiskTreatmentPlan riskTreatmentPlanFound = new RiskTreatmentPlan();
        Optional<ProcessEgsi> foundProcess = processRepository.findById(riskTreatmentPlanSubmitted.getIdProcess());
        if(!foundProcess.isPresent()){
            throw new RuntimeException("Process don't exists with ID: " + riskTreatmentPlanSubmitted.getIdProcess());   
        }
        riskTreatmentPlanFound = riskTreatmentPlanMapper.toEntity(riskTreatmentPlanSubmitted);
        riskTreatmentPlanFound.setProcess(foundProcess.get());
        RiskTreatmentPlan riskTreatmentPlanSaved = riskTreatmentPlanRepository.save(riskTreatmentPlanFound);
        return riskTreatmentPlanMapper.toDTO(riskTreatmentPlanSaved);
    }

    @Transactional
    public RiskTreatmentPlanDTO updateRiskTreatmentPlan(RiskTreatmentPlanDTO riskTreatmentPlanUpdate) {
        
        RiskTreatmentPlan riskTreatmentPlanModified = new RiskTreatmentPlan();
        RiskTreatmentPlan riskTreatmentPlanExisting = riskTreatmentPlanRepository.findById(riskTreatmentPlanUpdate.getIdPlanTratamient())
            .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + riskTreatmentPlanUpdate.getIdPlanTratamient()));
            if(riskTreatmentPlanUpdate.getInstitution() != null) riskTreatmentPlanExisting.setInstitution(riskTreatmentPlanUpdate.getInstitution());
            if(riskTreatmentPlanUpdate.getConfidentialityLevel() != null) riskTreatmentPlanExisting.setConfidentialityLevel(riskTreatmentPlanUpdate.getConfidentialityLevel());
            if(riskTreatmentPlanUpdate.getDateLastModification() != null) riskTreatmentPlanExisting.setDateLastModification(riskTreatmentPlanUpdate.getDateLastModification());
            if(riskTreatmentPlanUpdate.getDocumentResponsible() != null) riskTreatmentPlanExisting.setDocumentResponsible(riskTreatmentPlanUpdate.getDocumentResponsible());

        riskTreatmentPlanModified = riskTreatmentPlanRepository.save(riskTreatmentPlanExisting);
        log.info("Risk Treatment Plan updated with ID: {}", riskTreatmentPlanModified.getIdPlanTratamient());


        return riskTreatmentPlanMapper.toDTO(riskTreatmentPlanModified);
    }

    @Transactional
    public void deleteRiskTreatmentPlan(UUID idRiskTreatmentPlan) {
       log.info("Deleting Risk Treatment Plan with ID: {}", idRiskTreatmentPlan);
        RiskTreatmentPlan riskTreatmentPlanFound = riskTreatmentPlanRepository.findById(idRiskTreatmentPlan)
            .orElseThrow(() -> new RuntimeException("Risk Treatment Plan not found with ID: " + idRiskTreatmentPlan));
        riskTreatmentPlanRepository.delete(riskTreatmentPlanFound);
        log.info("Risk Treatment Plan with ID: {} has been deleted", idRiskTreatmentPlan);
    }

}
