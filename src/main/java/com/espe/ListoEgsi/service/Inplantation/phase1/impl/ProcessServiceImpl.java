package com.espe.ListoEgsi.service.Inplantation.phase1.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase1.ProcessEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.mapper.ProcessEgsiMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.service.Inplantation.phase1.ProcessService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    private static final Logger logger = LogManager.getLogger(ProcessService.class);
    

    @Autowired
    ProcessRepository processRepository;

    @Autowired
    ProcessEgsiMapper processEgsiMapper;


    @Override
    public List<ProcessEgsiDTO> findAllProcess() {
        List<ProcessEgsi> processFound = processRepository.findAll();
        return processFound
               .stream()
               .map(processEgsiMapper::toDTO)
               .collect(Collectors.toList());

    }

    @Override
    public ProcessEgsiDTO getProcess(UUID idProcess) {
        log.info("Fetching User with ID: {}", idProcess);
        return processRepository.findById(idProcess)
               .map(processEgsiMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Process not found with ID: " + idProcess));
    }

    @Override
    public ProcessEgsiDTO saveProcess(ProcessEgsiDTO processSubmit) {
        ProcessEgsi process = processEgsiMapper.toEntity(processSubmit);
        ProcessEgsi savedProcess = processRepository.save(process);
        return processEgsiMapper.toDTO(savedProcess);
    }

    @Transactional
    public ProcessEgsiDTO updateProcess(ProcessEgsiDTO processUpdate) {
       
        ProcessEgsi processModified = new ProcessEgsi();

        ProcessEgsi existingProcess = processRepository.findById(processUpdate.getIdProcess())
                .orElseThrow(() -> new RuntimeException("Process not found with ID: " + processUpdate.getIdProcess()));

                if(processUpdate.getRelatedProgram() != null) existingProcess.setRelatedProgram(processUpdate.getRelatedProgram());
                if(processUpdate.getProjectLeader() != null) existingProcess.setProjectLeader(processUpdate.getProjectLeader());
                if(processUpdate.getDateBegin() != null) existingProcess.setDateBegin(processUpdate.getDateBegin());
                if(processUpdate.getDateEnd() != null) existingProcess.setDateEnd(processUpdate.getDateEnd());
                if(processUpdate.getProblem() != null) existingProcess.setProblem(processUpdate.getProblem());
                if(processUpdate.getJustification() != null) existingProcess.setJustification(processUpdate.getJustification());
                if(processUpdate.getProjectDescription() != null) existingProcess.setProjectDescription(processUpdate.getProjectDescription());
                if(processUpdate.getBenefits() != null) existingProcess.setBenefits(processUpdate.getBenefits());
                if(processUpdate.getGeneralObjective() != null) existingProcess.setGeneralObjective(processUpdate.getGeneralObjective());
                if(processUpdate.getBeneficiaries() != null) existingProcess.setBeneficiaries(processUpdate.getBeneficiaries());
                if(processUpdate.getImplementationPeriod() != null) existingProcess.setImplementationPeriod(processUpdate.getImplementationPeriod());
                
                processModified = processRepository.save(existingProcess);
                log.info("Process with ID: {} has been updated.", processModified.getIdProcess());

        return processEgsiMapper.toDTO(processModified);
    }

    @Transactional
    public void deleteProcess(UUID idProcess) {
        log.info("Deleting Process with ID: {}", idProcess);
        ProcessEgsi processToDelete = processRepository.findById(idProcess)
                .orElseThrow(() -> new RuntimeException("Process not found with ID: " + idProcess));
        processRepository.delete(processToDelete);
        log.info("Process with ID: {} has been deleted.", idProcess);
    }

}
