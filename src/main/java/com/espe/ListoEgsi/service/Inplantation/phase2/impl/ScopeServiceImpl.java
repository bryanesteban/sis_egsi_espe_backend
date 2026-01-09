package com.espe.ListoEgsi.service.Inplantation.phase2.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase2.ScopeEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase2.ScopeEgsi;
import com.espe.ListoEgsi.mapper.ScopeMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase2.ScopeRepository;
import com.espe.ListoEgsi.service.Inplantation.phase2.ScopeService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScopeServiceImpl implements ScopeService {

    private static final Logger logger = LogManager.getLogger(ScopeService.class);


    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private ScopeMapper scopeMapper;

    @Autowired
    private ProcessRepository processRepository;
    

    @Override
    public List<ScopeEgsiDTO> findAllScopes() {
        List<ScopeEgsi> scopesFound = scopeRepository.findAll();
        return scopesFound
               .stream()
               .map(scopeMapper::toDTO)
               .toList();
    }

    @Override
    public ScopeEgsiDTO getScope(UUID idScope) {
        logger.info("Fetching Scope with ID: {}", idScope);
        return scopeRepository.findById(idScope)
               .map(scopeMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Scope not found with ID: " + idScope));
    }

    @Override
    public ScopeEgsiDTO saveScope(ScopeEgsiDTO scopeSubmit) {
        ScopeEgsi scope = scopeMapper.toEntity(scopeSubmit);
        Optional<ProcessEgsi> foundProcess = processRepository.findById(scopeSubmit.getIdProcess());
                if (!foundProcess.isPresent()) {
                    throw new RuntimeException("Process not found with ID: " + scopeSubmit.getIdProcess());
                }
                scope.setProcess(foundProcess.get());

        ScopeEgsi savedScope = scopeRepository.save(scope);
        return scopeMapper.toDTO(savedScope);
    }

    @Transactional
    public ScopeEgsiDTO updateScope(ScopeEgsiDTO scopeUpdate) {
        ScopeEgsi scopeModified = new ScopeEgsi();

        ScopeEgsi existingScope = scopeRepository.findById(scopeUpdate.getIdScope())
                .orElseThrow(() -> new RuntimeException("Scope not found with ID: " + scopeUpdate.getIdScope()));

                if(scopeUpdate.getScopeDescription() != null) existingScope.setScopeDescription(scopeUpdate.getScopeDescription());
                if(scopeUpdate.getObjetiveScope() != null) existingScope.setObjetiveScope(scopeUpdate.getObjetiveScope());
                if(scopeUpdate.getUsersScope() != null) existingScope.setUsersScope(scopeUpdate.getUsersScope());
                if(scopeUpdate.getReviewAndUpdate() != null) existingScope.setReviewAndUpdate(scopeUpdate.getReviewAndUpdate());
                if(scopeUpdate.getReferenceDocuments() != null) existingScope.setReferenceDocuments(scopeUpdate.getReferenceDocuments());
                if(scopeUpdate.getScopeDefinition() != null) existingScope.setScopeDefinition(scopeUpdate.getScopeDefinition());
                if(scopeUpdate.getProcessAndServices() != null) existingScope.setProcessAndServices(scopeUpdate.getProcessAndServices());
                if(scopeUpdate.getOrganizationUnits() != null) existingScope.setOrganizationUnits(scopeUpdate.getOrganizationUnits());
                if(scopeUpdate.getUbicationDescription() != null) existingScope.setUbicationDescription(scopeUpdate.getUbicationDescription());
                if(scopeUpdate.getUbicationTable() != null) existingScope.setUbicationTable(scopeUpdate.getUbicationTable());
                if(scopeUpdate.getNetworksAndInfraestructureTi() != null) existingScope.setNetworksAndInfraestructureTi(scopeUpdate.getNetworksAndInfraestructureTi());
                if(scopeUpdate.getExclusion() != null) existingScope.setExclusion(scopeUpdate.getExclusion());
                if(scopeUpdate.getState() != null) existingScope.setState(scopeUpdate.getState());

                scopeModified = scopeRepository.save(existingScope);
                log.info("Scope with ID: {} has been updated.", scopeModified.getIdScope());

        return scopeMapper.toDTO(scopeModified);
    }

    @Transactional
    public void deleteScope(UUID idScope) {
        logger.info("Deleting Scope with ID: {}", idScope);
        ScopeEgsi scopeToDelete = scopeRepository.findById(idScope)
                .orElseThrow(() -> new RuntimeException("Scope not found with ID: " + idScope));
        scopeRepository.delete(scopeToDelete);
        logger.info("Scope with ID: {} has been deleted.", idScope);
    }


}
