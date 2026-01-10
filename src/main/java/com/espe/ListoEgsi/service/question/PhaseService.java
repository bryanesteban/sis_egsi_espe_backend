package com.espe.ListoEgsi.service.question;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.question.PhaseDTO;

public interface PhaseService {
    
    PhaseDTO createPhase(PhaseDTO phaseDTO);
    
    PhaseDTO updatePhase(UUID id, PhaseDTO phaseDTO);
    
    void deletePhase(UUID id);
    
    PhaseDTO getPhaseById(UUID id);
    
    List<PhaseDTO> getAllPhases();
    
    List<PhaseDTO> getPhasesByProcess(UUID idProcess);
    
    List<PhaseDTO> getPhasesByStatus(String status);
}
