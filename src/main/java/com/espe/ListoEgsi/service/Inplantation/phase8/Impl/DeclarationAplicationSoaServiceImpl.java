package com.espe.ListoEgsi.service.Inplantation.phase8.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.DeclarationAplicationSoaDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase7.ReportEvaluationOfTreatmentRisk;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.DeclarationAplicationSoa;
import com.espe.ListoEgsi.mapper.DeclarationAplicationSoaMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase8.DeclarationAplicationSoaRepository;
import com.espe.ListoEgsi.service.Inplantation.phase8.DeclarationAplicationSoaService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeclarationAplicationSoaServiceImpl implements DeclarationAplicationSoaService {

    @Autowired
    private DeclarationAplicationSoaRepository declarationAplicationSoaRepository;

    @Autowired
    private DeclarationAplicationSoaMapper declarationAplicationSoaMapper;

    @Autowired
    private ProcessRepository processRepository;



    @Override
    public List<DeclarationAplicationSoaDTO> findAllDeclarationAplicationSoas() {
        List<DeclarationAplicationSoa> reportEvaluationRiskFound = declarationAplicationSoaRepository.findAll();
        return reportEvaluationRiskFound
                .stream()
                .map(declarationAplicationSoaMapper::toDTO)
                .toList();
        
    }

    @Override
    public DeclarationAplicationSoaDTO getDeclarationAplicationSoa(UUID idDeclarationAplicationSoa) {

        log.info("Fetching Declaration Aplication Soa with ID: {}", idDeclarationAplicationSoa);
        return declarationAplicationSoaRepository.findById(idDeclarationAplicationSoa)
               .map(declarationAplicationSoaMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Declaration Aplication Soa not found with ID: " + idDeclarationAplicationSoa));

    }

    @Override
    public DeclarationAplicationSoaDTO saveDeclarationAplicationSoa(
            DeclarationAplicationSoaDTO declarationAplicationSoaSubmit) { 
        DeclarationAplicationSoa declarationAplicationSoaFound = new DeclarationAplicationSoa();
        Optional<ProcessEgsi> foundProcess = processRepository.findById(declarationAplicationSoaSubmit.getIdProcess());
        if(!foundProcess.isPresent()){
            throw new RuntimeException("Report Evaluation Of Treatment Risk don't exists with ID: " + declarationAplicationSoaSubmit.getIdProcess());   
        }
        declarationAplicationSoaFound = declarationAplicationSoaMapper.toEntity(declarationAplicationSoaSubmit);
        declarationAplicationSoaFound.setProcess(foundProcess.get());
        DeclarationAplicationSoa declarationAplicationSoaSaved = declarationAplicationSoaRepository.save(declarationAplicationSoaFound);
        return declarationAplicationSoaMapper.toDTO(declarationAplicationSoaSaved);
    }

    @Transactional
    public DeclarationAplicationSoaDTO updateDeclarationAplicationSoa(
            DeclarationAplicationSoaDTO declarationAplicationSoaUpdate) {

        DeclarationAplicationSoa declarationAplicationSoaModified = new DeclarationAplicationSoa();
        DeclarationAplicationSoa declarationAplicationSoaExisting = declarationAplicationSoaRepository.findById(declarationAplicationSoaUpdate.getIdDeclaration())
            .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + declarationAplicationSoaUpdate.getIdDeclaration()));   
        if(declarationAplicationSoaUpdate.getInstitute() != null) declarationAplicationSoaExisting.setInstitute(declarationAplicationSoaUpdate.getInstitute());
        if(declarationAplicationSoaUpdate.getNumberController() != null) declarationAplicationSoaExisting.setNumberController(declarationAplicationSoaUpdate.getNumberController());
        if(declarationAplicationSoaUpdate.getLevelConfidentiality() != null) declarationAplicationSoaExisting.setLevelConfidentiality(declarationAplicationSoaUpdate.getLevelConfidentiality());
        if(declarationAplicationSoaUpdate.getDocumentResponsible() != null) declarationAplicationSoaExisting.setDocumentResponsible(declarationAplicationSoaUpdate.getDocumentResponsible());

        declarationAplicationSoaModified = declarationAplicationSoaRepository.save(declarationAplicationSoaExisting);
        log.info("Declaration Aplication Soa with ID: {} has been updated", declarationAplicationSoaModified.getIdDeclaration());

        return declarationAplicationSoaMapper.toDTO(declarationAplicationSoaModified);
        }

    @Transactional
    public void deleteDeclarationAplicationSoa(UUID idDeclarationAplicationSoa) {
        log.info("Deleting Declaration Aplication Soa with ID: {}", idDeclarationAplicationSoa);
        DeclarationAplicationSoa declarationAplicationSoaExisting = declarationAplicationSoaRepository.findById(idDeclarationAplicationSoa)
            .orElseThrow(() -> new RuntimeException("Declaration Aplication Soa not found with ID: " + idDeclarationAplicationSoa));
        declarationAplicationSoaRepository.delete(declarationAplicationSoaExisting);
        log.info("Declaration Aplication Soa with ID: {} has been deleted", idDeclarationAplicationSoa);
    }

}
