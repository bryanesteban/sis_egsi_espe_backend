package com.espe.ListoEgsi.service.Inplantation.phase6.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase6.MethodologyEvaluationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase6.MethodologyEvaluation;
import com.espe.ListoEgsi.mapper.MethodologyEvaluationMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase6.MethodologyEvaluationRepository;
import com.espe.ListoEgsi.service.Inplantation.phase6.MethodologyEvaluationService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MethodologyEvaluationServiceImpl implements MethodologyEvaluationService {
    
    @Autowired
    private MethodologyEvaluationRepository methodologyEvaluationRepository;

    @Autowired
    private MethodologyEvaluationMapper methodologyEvaluationMapper;

    @Autowired
    private ProcessRepository processRepository;
    
    
    
    @Override
    public List<MethodologyEvaluationDTO> findAllMethodologyEvaluations() {
      List<MethodologyEvaluation> methodologyEvaluationsFound = methodologyEvaluationRepository.findAll();
      return methodologyEvaluationsFound
              .stream()
              .map(methodologyEvaluationMapper::toDTO)
              .toList();
    }

    @Override
    public MethodologyEvaluationDTO getMethodologyEvaluation(UUID idMethodologyEval) {
        log.info("Fetching Methodology Evaluation with ID: {}", idMethodologyEval);
        return methodologyEvaluationRepository.findById(idMethodologyEval)
               .map(methodologyEvaluationMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Methodology Evaluation not found with ID: " + idMethodologyEval));    
    }

    @Override
    public MethodologyEvaluationDTO saveMethodologyEvaluation(
             MethodologyEvaluationDTO methodologyEvaluationSubmit) {
        MethodologyEvaluation methodologyEvaluationFound = new MethodologyEvaluation();
        Optional<ProcessEgsi> foundProcess = processRepository.findById(methodologyEvaluationSubmit.getIdProcess());
        if(!foundProcess.isPresent()){
            throw new RuntimeException("Methodology Evaluation don't exists with ID: " + methodologyEvaluationSubmit.getIdProcess());
        }
        methodologyEvaluationFound = methodologyEvaluationMapper.toEntity(methodologyEvaluationSubmit);
        methodologyEvaluationFound.setProcess(foundProcess.get());
        MethodologyEvaluation methodologyEvaluationSaved = methodologyEvaluationRepository.save(methodologyEvaluationFound);
        return methodologyEvaluationMapper.toDTO(methodologyEvaluationSaved);
    }

    @Transactional
    public MethodologyEvaluationDTO updateMethodologyEvaluation(MethodologyEvaluationDTO methodologyEvaluationUpdate) {
        
        MethodologyEvaluation methodologyEvaluationModified = new MethodologyEvaluation();
        MethodologyEvaluation methodologyEvaluationExisting = methodologyEvaluationRepository.findById(methodologyEvaluationUpdate.getIdMethodologyEval())
            .orElseThrow(() -> new RuntimeException("Methodology Evaluation not found with ID: " + methodologyEvaluationUpdate.getIdMethodologyEval()));

        if(methodologyEvaluationUpdate.getObjetive()!=null) methodologyEvaluationExisting.setObjetive(methodologyEvaluationUpdate.getObjetive());
        if(methodologyEvaluationUpdate.getScope()!=null) methodologyEvaluationExisting.setScope(methodologyEvaluationUpdate.getScope());
        if(methodologyEvaluationUpdate.getUsers()!=null) methodologyEvaluationExisting.setUsers(methodologyEvaluationUpdate.getUsers());    
        if(methodologyEvaluationUpdate.getProcessMethodology()!=null) methodologyEvaluationExisting.setProcessMethodology(methodologyEvaluationUpdate.getProcessMethodology());
        if(methodologyEvaluationUpdate.getAssetsMethodology()!=null) methodologyEvaluationExisting.setAssetsMethodology(methodologyEvaluationUpdate.getAssetsMethodology());
        if(methodologyEvaluationUpdate.getIdentificationMethodology()!=null) methodologyEvaluationExisting.setIdentificationMethodology(methodologyEvaluationUpdate.getIdentificationMethodology());
        if(methodologyEvaluationUpdate.getImpactProbability()!=null) methodologyEvaluationExisting.setImpactProbability(methodologyEvaluationUpdate.getImpactProbability());
        if(methodologyEvaluationUpdate.getAcceptanceCriteriaMethodology()!=null) methodologyEvaluationExisting.setAcceptanceCriteriaMethodology(methodologyEvaluationUpdate.getAcceptanceCriteriaMethodology());
        if(methodologyEvaluationUpdate.getRiskTreatmentMethodology()!=null) methodologyEvaluationExisting.setRiskTreatmentMethodology(methodologyEvaluationUpdate.getRiskTreatmentMethodology());
        if(methodologyEvaluationUpdate.getRevisionPeriodMethodology()!=null) methodologyEvaluationExisting.setRevisionPeriodMethodology(methodologyEvaluationUpdate.getRevisionPeriodMethodology());
        if(methodologyEvaluationUpdate.getDeclarationApplycabilityMethodology()!=null) methodologyEvaluationExisting.setDeclarationApplycabilityMethodology(methodologyEvaluationUpdate.getDeclarationApplycabilityMethodology());
        if(methodologyEvaluationUpdate.getReportsMethodology()!=null) methodologyEvaluationExisting.setReportsMethodology(methodologyEvaluationUpdate.getReportsMethodology());
        if(methodologyEvaluationUpdate.getValidityManagementMethodology()!=null) methodologyEvaluationExisting.setValidityManagementMethodology(methodologyEvaluationUpdate.getValidityManagementMethodology());
        if(methodologyEvaluationUpdate.getNotesMethodology()!=null) methodologyEvaluationExisting.setNotesMethodology(methodologyEvaluationUpdate.getNotesMethodology());

        methodologyEvaluationModified = methodologyEvaluationRepository.save(methodologyEvaluationExisting);
        log.info("Methodology Evaluation updated with ID: {}", methodologyEvaluationModified.getIdMethodologyEval());

        return methodologyEvaluationMapper.toDTO(methodologyEvaluationModified);
        
    }

    @Transactional
    public void deleteMethodologyEvaluation(UUID idMethodologyEval) {
        log.info("Deleting Methodology Evaluation with ID: {}", idMethodologyEval);
        MethodologyEvaluation methodologyEvaluationToDelete = methodologyEvaluationRepository.findById(idMethodologyEval)
            .orElseThrow(() -> new RuntimeException("Methodology Evaluation not found with ID: " + idMethodologyEval));
        methodologyEvaluationRepository.delete(methodologyEvaluationToDelete);
        log.info("Methodology Evaluation with ID: {} has been deleted.", idMethodologyEval);
    }


}
