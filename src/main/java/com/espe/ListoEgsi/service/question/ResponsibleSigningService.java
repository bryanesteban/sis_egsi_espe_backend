package com.espe.ListoEgsi.service.question;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.question.ResponsibleSigningDTO;

public interface ResponsibleSigningService {
    
    ResponsibleSigningDTO createResponsibleSigning(ResponsibleSigningDTO responsibleSigningDTO);
    
    ResponsibleSigningDTO updateResponsibleSigning(UUID id, ResponsibleSigningDTO responsibleSigningDTO);
    
    void deleteResponsibleSigning(UUID id);
    
    ResponsibleSigningDTO getResponsibleSigningById(UUID id);
    
    List<ResponsibleSigningDTO> getAllResponsibleSignings();
    
    List<ResponsibleSigningDTO> getResponsibleSigningsByPhase(UUID idPhase);
}
