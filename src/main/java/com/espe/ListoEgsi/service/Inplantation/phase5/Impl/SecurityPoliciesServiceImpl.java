package com.espe.ListoEgsi.service.Inplantation.phase5.Impl;

import java.security.Security;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase5.SecurityPoliciesDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase5.SecurityPolicies;
import com.espe.ListoEgsi.mapper.SecurityPoliciesMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase5.SecurityPoliciesRepository;
import com.espe.ListoEgsi.service.Inplantation.phase5.SecurityPoliciesService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityPoliciesServiceImpl implements SecurityPoliciesService {

    @Autowired
    private SecurityPoliciesRepository securityPoliciesRepository;

    @Autowired
    private SecurityPoliciesMapper securityPoliciesMapper;

    @Autowired
    private ProcessRepository processRepository;



    @Override
    public List<SecurityPoliciesDTO> findAllSecurityPolicies() {
        List<SecurityPolicies> securityPoliciesFound = securityPoliciesRepository.findAll();
        return securityPoliciesFound
                .stream()
                .map(securityPoliciesMapper::toDTO)
                .toList();
    }

    @Override
    public SecurityPoliciesDTO getSecurityPolicies(UUID idSecurityPolicies) {
        log.info("Fetching Security Policies with ID: {}", idSecurityPolicies);
        return securityPoliciesRepository.findById(idSecurityPolicies)
               .map(securityPoliciesMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Security Policies not found with ID: " + idSecurityPolicies));
    }

    @Override
    public SecurityPoliciesDTO saveSecurityPolicies(SecurityPoliciesDTO securityPoliciesSubmit) {
        SecurityPolicies securityPoliciesFound = new SecurityPolicies();
        Optional<ProcessEgsi> foundProcess = processRepository.findById(securityPoliciesSubmit.getIdProcess());
        if(!foundProcess.isPresent()){
            throw new RuntimeException("Security Policies already exists with ID: " + securityPoliciesSubmit.getIdProcess());
        }
        securityPoliciesFound = securityPoliciesMapper.toEntity(securityPoliciesSubmit);
        securityPoliciesFound.setProcess(foundProcess.get());
        SecurityPolicies securityPoliciesSaved = securityPoliciesRepository.save(securityPoliciesFound);
        return securityPoliciesMapper.toDTO(securityPoliciesSaved);
    }

    @Transactional
    public SecurityPoliciesDTO updateSecurityPolicies(SecurityPoliciesDTO securityPoliciesUpdate) {
     
        SecurityPolicies securityPoliciesModified = new SecurityPolicies();
        SecurityPolicies securityPoliciesExisting = securityPoliciesRepository.findById(securityPoliciesUpdate.getIdSecurityPolicies())
            .orElseThrow(() -> new RuntimeException("Security Policies not found with ID: " + securityPoliciesUpdate.getIdSecurityPolicies()));

        if(securityPoliciesUpdate.getBackgrounds() != null) securityPoliciesExisting.setBackgrounds(securityPoliciesUpdate.getBackgrounds());
        if(securityPoliciesUpdate.getPolicyObjetive() != null) securityPoliciesExisting.setPolicyObjetive(securityPoliciesUpdate.getPolicyObjetive());
        if(securityPoliciesUpdate.getDescriptionPolicy() != null) securityPoliciesExisting.setDescriptionPolicy(securityPoliciesUpdate.getDescriptionPolicy());
        if(securityPoliciesUpdate.getDeclarationObjetiveSecurity() != null) securityPoliciesExisting.setDeclarationObjetiveSecurity(securityPoliciesUpdate.getDeclarationObjetiveSecurity());
        if(securityPoliciesUpdate.getRolesAndResponsabilities() != null) securityPoliciesExisting.setRolesAndResponsabilities(securityPoliciesUpdate.getRolesAndResponsabilities());
        if(securityPoliciesUpdate.getScopeAndUsers() != null) securityPoliciesExisting.setScopeAndUsers(securityPoliciesUpdate.getScopeAndUsers());
        if(securityPoliciesUpdate.getComunicationPolicy() != null) securityPoliciesExisting.setComunicationPolicy(securityPoliciesUpdate.getComunicationPolicy());
        if(securityPoliciesUpdate.getExceptionSanctions() != null) securityPoliciesExisting.setExceptionSanctions(securityPoliciesUpdate.getExceptionSanctions());
        if(securityPoliciesUpdate.getGlosaryTermsTable() != null) securityPoliciesExisting.setGlosaryTermsTable(securityPoliciesUpdate.getGlosaryTermsTable());
        if(securityPoliciesUpdate.getReferenceDocuments() != null) securityPoliciesExisting.setReferenceDocuments(securityPoliciesUpdate.getReferenceDocuments());

        securityPoliciesModified = securityPoliciesRepository.save(securityPoliciesExisting);
        log.info("Security Policies with ID: {} has been updated.", securityPoliciesUpdate.getIdSecurityPolicies());
        
        return securityPoliciesMapper.toDTO(securityPoliciesModified);
    }

    @Transactional
    public void deleteSecurityPolicies(UUID idSecurityPolicies) {
        log.info("Deleting Security Policies with ID: {}", idSecurityPolicies);
        SecurityPolicies securityPoliciesToDelete = securityPoliciesRepository.findById(idSecurityPolicies)
                    .orElseThrow(() -> new RuntimeException("Security Policies not found with ID: " + idSecurityPolicies));
            securityPoliciesRepository.delete(securityPoliciesToDelete);
            log.info("Security Policies with ID: {} has been deleted.", idSecurityPolicies);
    }

}
