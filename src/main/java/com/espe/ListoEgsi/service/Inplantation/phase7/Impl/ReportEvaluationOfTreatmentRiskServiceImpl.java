package com.espe.ListoEgsi.service.Inplantation.phase7.Impl;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase7.ReportEvaluationOfTreatmentRiskDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase7.ReportEvaluationOfTreatmentRisk;
import com.espe.ListoEgsi.mapper.ReportEvaluationOfTreatmentRiskMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase7.ReportEvaluationOfTreatmentRiskRepository;
import com.espe.ListoEgsi.service.Inplantation.phase7.ReportEvaluationOfTreatmentRiskService;

import lombok.extern.slf4j.Slf4j;




@Service
@Slf4j
public class ReportEvaluationOfTreatmentRiskServiceImpl implements ReportEvaluationOfTreatmentRiskService {


    @Autowired
    private ReportEvaluationOfTreatmentRiskRepository reportEvaluationRiskRepository;

    @Autowired
    private ReportEvaluationOfTreatmentRiskMapper reportEvaluationRiskMapper;

    @Autowired
    private ProcessRepository processRepository;

    @Override
    public List<ReportEvaluationOfTreatmentRiskDTO> findAllReportEvaluationOfTreatmentRisks() {
        List<ReportEvaluationOfTreatmentRisk> reportEvaluationRiskFound = reportEvaluationRiskRepository.findAll();
        return reportEvaluationRiskFound
                .stream()
                .map(reportEvaluationRiskMapper::toDTO)
                .toList();
    }

    @Override
    public ReportEvaluationOfTreatmentRiskDTO getReportEvaluationOfTreatmentRisk(UUID idReportEvaluationRisk) {
        log.info("Fetching Methodology Evaluation with ID: {}", idReportEvaluationRisk);
        return reportEvaluationRiskRepository.findById(idReportEvaluationRisk)
               .map(reportEvaluationRiskMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + idReportEvaluationRisk));

        
    }

    @Override
    public ReportEvaluationOfTreatmentRiskDTO saveReportEvaluationOfTreatmentRisk(
            ReportEvaluationOfTreatmentRiskDTO ReportEvaluationRiskSubmit) {
        ReportEvaluationOfTreatmentRisk reportEvaluationRiskFound = new ReportEvaluationOfTreatmentRisk();
        Optional<ProcessEgsi> foundProcess = processRepository.findById(ReportEvaluationRiskSubmit.getIdProcess());
        if(!foundProcess.isPresent()){
            throw new RuntimeException("Report Evaluation Of Treatment Risk don't exists with ID: " + ReportEvaluationRiskSubmit.getIdProcess());   
        }
        reportEvaluationRiskFound = reportEvaluationRiskMapper.toEntity(ReportEvaluationRiskSubmit);
        ReportEvaluationOfTreatmentRisk reportEvaluationRiskSaved = reportEvaluationRiskRepository.save(reportEvaluationRiskFound);
        return reportEvaluationRiskMapper.toDTO(reportEvaluationRiskSaved);
             
    }

    @Override
    public ReportEvaluationOfTreatmentRiskDTO updateReportEvaluationOfTreatmentRisk(
            ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskUpdate) {
        ReportEvaluationOfTreatmentRisk reportEvaluationRiskModified = new ReportEvaluationOfTreatmentRisk();
        ReportEvaluationOfTreatmentRisk reportEvaluationRiskExisting = reportEvaluationRiskRepository.findById(reportEvaluationRiskUpdate.getIdReportTreatment())
            .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + reportEvaluationRiskUpdate.getIdReportTreatment()));   
        if(reportEvaluationRiskUpdate.getObjetive()!=null) reportEvaluationRiskExisting.setObjetive(reportEvaluationRiskUpdate.getObjetive());
        if(reportEvaluationRiskUpdate.getDescription()!=null) reportEvaluationRiskExisting.setDescription(reportEvaluationRiskUpdate.getDescription());
        if(reportEvaluationRiskUpdate.getContextEvaluation()!=null) reportEvaluationRiskExisting.setContextEvaluation(reportEvaluationRiskUpdate.getContextEvaluation());
        if(reportEvaluationRiskUpdate.getMethodologyEvaluation()!=null) reportEvaluationRiskExisting.setMethodologyEvaluation(reportEvaluationRiskUpdate.getMethodologyEvaluation());
        if(reportEvaluationRiskUpdate.getIdentificationValorationAssets()!=null) reportEvaluationRiskExisting.setIdentificationValorationAssets(reportEvaluationRiskUpdate.getIdentificationValorationAssets());
        if(reportEvaluationRiskUpdate.getIdentificationRiskAndVulnerabilities()!=null) reportEvaluationRiskExisting.setIdentificationRiskAndVulnerabilities(reportEvaluationRiskUpdate.getIdentificationRiskAndVulnerabilities());
        if(reportEvaluationRiskUpdate.getIdentificationControl()!=null) reportEvaluationRiskExisting.setIdentificationControl(reportEvaluationRiskUpdate.getIdentificationControl());
        if(reportEvaluationRiskUpdate.getEvaluationRisk()!=null) reportEvaluationRiskExisting.setEvaluationRisk(reportEvaluationRiskUpdate.getEvaluationRisk());
        if(reportEvaluationRiskUpdate.getTreatmentRisk()!=null) reportEvaluationRiskExisting.setTreatmentRisk(reportEvaluationRiskUpdate.getTreatmentRisk());
        if(reportEvaluationRiskUpdate.getNivelRisk()!=null) reportEvaluationRiskExisting.setNivelRisk(reportEvaluationRiskUpdate.getNivelRisk());
        if(reportEvaluationRiskUpdate.getClarificationNote()!=null) reportEvaluationRiskExisting.setClarificationNote(reportEvaluationRiskUpdate.getClarificationNote());
        reportEvaluationRiskModified = reportEvaluationRiskRepository.save(reportEvaluationRiskExisting);
        log.info("Report Evaluation Of Treatment Risk with ID: {} has been updated", reportEvaluationRiskModified.getIdReportTreatment());

        return reportEvaluationRiskMapper.toDTO(reportEvaluationRiskModified);


        }

    @Override
    public void deleteReportEvaluationOfTreatmentRisk(UUID idReportEvaluationRisk) {
        log.info("Deleting Report Evaluation Of Treatment Risk with ID: {}", idReportEvaluationRisk);
        ReportEvaluationOfTreatmentRisk reportEvaluationRiskExisting = reportEvaluationRiskRepository.findById(idReportEvaluationRisk)
            .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + idReportEvaluationRisk));
        reportEvaluationRiskRepository.delete(reportEvaluationRiskExisting);
        log.info("Report Evaluation Of Treatment Risk with ID: {} has been deleted", idReportEvaluationRisk);
    }

}
