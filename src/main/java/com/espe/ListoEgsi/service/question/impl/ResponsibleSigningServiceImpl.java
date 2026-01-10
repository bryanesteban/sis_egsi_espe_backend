package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.ResponsibleSigningDTO;
import com.espe.ListoEgsi.domain.model.entity.question.ResponsibleSigning;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.ResponsibleSigningMapper;
import com.espe.ListoEgsi.repository.question.ResponsibleSigningRepository;
import com.espe.ListoEgsi.service.question.ResponsibleSigningService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponsibleSigningServiceImpl implements ResponsibleSigningService {

    private final ResponsibleSigningRepository responsibleSigningRepository;
    private final ResponsibleSigningMapper responsibleSigningMapper;

    @Override
    @Transactional
    public ResponsibleSigningDTO createResponsibleSigning(ResponsibleSigningDTO responsibleSigningDTO) {
        ResponsibleSigning responsibleSigning = responsibleSigningMapper.toEntity(responsibleSigningDTO);
        ResponsibleSigning savedResponsibleSigning = responsibleSigningRepository.save(responsibleSigning);
        return responsibleSigningMapper.toDTO(savedResponsibleSigning);
    }

    @Override
    @Transactional
    public ResponsibleSigningDTO updateResponsibleSigning(UUID id, ResponsibleSigningDTO responsibleSigningDTO) {
        ResponsibleSigning existingResponsibleSigning = responsibleSigningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResponsibleSigning not found with id: " + id));
        
        existingResponsibleSigning.setNameResponsible(responsibleSigningDTO.getNameResponsible());
        existingResponsibleSigning.setStatusSign(responsibleSigningDTO.getStatusSign());
        existingResponsibleSigning.setUpdatedAt(responsibleSigningDTO.getUpdatedAt());
        
        ResponsibleSigning updatedResponsibleSigning = responsibleSigningRepository.save(existingResponsibleSigning);
        return responsibleSigningMapper.toDTO(updatedResponsibleSigning);
    }

    @Override
    @Transactional
    public void deleteResponsibleSigning(UUID id) {
        if (!responsibleSigningRepository.existsById(id)) {
            throw new ResourceNotFoundException("ResponsibleSigning not found with id: " + id);
        }
        responsibleSigningRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponsibleSigningDTO getResponsibleSigningById(UUID id) {
        ResponsibleSigning responsibleSigning = responsibleSigningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResponsibleSigning not found with id: " + id));
        return responsibleSigningMapper.toDTO(responsibleSigning);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponsibleSigningDTO> getAllResponsibleSignings() {
        return responsibleSigningRepository.findAll().stream()
                .map(responsibleSigningMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponsibleSigningDTO> getResponsibleSigningsByPhase(UUID idPhase) {
        return responsibleSigningRepository.findByPhase_IdPhase(idPhase).stream()
                .map(responsibleSigningMapper::toDTO)
                .collect(Collectors.toList());
    }
}
