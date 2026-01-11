package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.PhaseDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.question.Phase;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.PhaseMapper;
import com.espe.ListoEgsi.repository.question.PhaseRepository;
import com.espe.ListoEgsi.service.question.PhaseService;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    PhaseRepository phaseRepository;

    @Autowired
    PhaseMapper phaseMapper;

    @Autowired
    ProcessRepository processRepository;


    @Override
    @Transactional
    public PhaseDTO createPhase(PhaseDTO phaseDTO) {
        Phase phaseCustom = phaseMapper.toEntity(phaseDTO);
        ProcessEgsi process = processRepository.findById(phaseDTO.getIdProcess())
                .orElseThrow(() -> new ResourceNotFoundException("Process not found with id: " + phaseDTO.getIdProcess()));
        phaseCustom.setProcess(process);
        Phase savedPhase = phaseRepository.save(phaseCustom);
        return phaseMapper.toDTO(savedPhase);
    }

    @Override
    @Transactional
    public PhaseDTO updatePhase(UUID id, PhaseDTO phaseDTO) {
        Phase existingPhase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id: " + id));
        
        existingPhase.setQuestionaryCode(phaseDTO.getQuestionaryCode());
        existingPhase.setResponsibles(phaseDTO.getResponsibles());
        existingPhase.setStatus(phaseDTO.getStatus());
        
        Phase updatedPhase = phaseRepository.save(existingPhase);
        return phaseMapper.toDTO(updatedPhase);
    }

    @Override
    @Transactional
    public void deletePhase(UUID id) {
        if (!phaseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Phase not found with id: " + id);
        }
        phaseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PhaseDTO getPhaseById(UUID id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id: " + id));
        return phaseMapper.toDTO(phase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseDTO> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseDTO> getPhasesByProcess(UUID idProcess) {
        return phaseRepository.findByProcess_IdProcess(idProcess).stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseDTO> getPhasesByStatus(String status) {
        return phaseRepository.findByStatus(status).stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }
}
