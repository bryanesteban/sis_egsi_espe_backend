package com.espe.ListoEgsi.service.Inplantation.phase8.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.AnexoEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.AnexoEgsi;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.DeclarationAplicationSoa;
import com.espe.ListoEgsi.mapper.AnexoEgsiMapper;
import com.espe.ListoEgsi.repository.Inplantation.phase8.AnexoEgsiRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase8.DeclarationAplicationSoaRepository;
import com.espe.ListoEgsi.service.Inplantation.phase8.AnexoEgsiService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnexoEgsiServiceImpl implements AnexoEgsiService {


    @Autowired
    private AnexoEgsiRepository anexoEgsiRepository;

    @Autowired
    private AnexoEgsiMapper anexoEgsiMapper;

    @Autowired
    private DeclarationAplicationSoaRepository declarationAplicationSoaRepository;

    @Override
    public List<AnexoEgsiDTO> findAllAnexoEgis() {
        List<AnexoEgsi> anexosEgsi = anexoEgsiRepository.findAll();
        return anexosEgsi
                .stream()
                .map(anexoEgsiMapper::toDTO)
                .toList(); 
    }

    @Override
    public AnexoEgsiDTO getAnexoEgsi(UUID idAnexoEgsi) {

        log.info("Fetching Anexo Egsi with ID: {}", idAnexoEgsi);
        return anexoEgsiRepository.findById(idAnexoEgsi)
               .map(anexoEgsiMapper::toDTO)
               .orElseThrow(() -> new RuntimeException("Anexo Egsi not found with ID: " + idAnexoEgsi));

   }

    @Override
    public AnexoEgsiDTO saveAnexoEgsi(AnexoEgsiDTO anexoEgsiSubmited) {
         AnexoEgsi anexoEgsiFound = new AnexoEgsi();
        Optional<DeclarationAplicationSoa> foundDeclaration = declarationAplicationSoaRepository.findById(anexoEgsiSubmited.getIdDeclaration());
        if(!foundDeclaration.isPresent()){
            throw new RuntimeException("Declaration Aplication Soa don't exists with ID: " + anexoEgsiSubmited.getIdDeclaration());   
        }
        anexoEgsiFound = anexoEgsiMapper.toEntity(anexoEgsiSubmited);
        anexoEgsiFound.setDeclaration(foundDeclaration.get());
        AnexoEgsi anexoEgsiSaved = anexoEgsiRepository.save(anexoEgsiFound);
        return anexoEgsiMapper.toDTO(anexoEgsiSaved);
    
    }

    @Transactional
    public AnexoEgsiDTO updateAnexoEgsi(AnexoEgsiDTO anexoEgsiUpdate) {
        
        AnexoEgsi anexoEgsiModified = new AnexoEgsi();
        AnexoEgsi anexoEgsiExisting = anexoEgsiRepository.findById(anexoEgsiUpdate.getIdAnexoEgsi())
            .orElseThrow(() -> new RuntimeException("Report Evaluation Of Treatment Risk not found with ID: " + anexoEgsiUpdate.getIdAnexoEgsi()));   
        if(anexoEgsiUpdate.getItem() != null) anexoEgsiExisting.setItem(anexoEgsiUpdate.getItem());
        if(anexoEgsiUpdate.getSection() != null) anexoEgsiExisting.setSection(anexoEgsiUpdate.getSection());
        if(anexoEgsiUpdate.getDescription() != null) anexoEgsiExisting.setDescription(anexoEgsiUpdate.getDescription());
        if(anexoEgsiUpdate.getActualState() != null) anexoEgsiExisting.setActualState(anexoEgsiUpdate.getActualState());
        if(anexoEgsiUpdate.getApplies() != null) anexoEgsiExisting.setApplies(anexoEgsiUpdate.getApplies());
        if(anexoEgsiUpdate.getSelectionJustify() != null) anexoEgsiExisting.setSelectionJustify(anexoEgsiUpdate.getSelectionJustify());
        if(anexoEgsiUpdate.getObservation() != null) anexoEgsiExisting.setObservation(anexoEgsiUpdate.getObservation());

        anexoEgsiModified = anexoEgsiRepository.save(anexoEgsiExisting);
        log.info("Declaration Aplication Soa with ID: {} has been updated", anexoEgsiModified.getIdAnexoEgsi());
        
        return anexoEgsiMapper.toDTO(anexoEgsiModified);
    }

    @Override
    public void deleteAnexoEgsi(UUID idAnexoEgsi) {
        log.info("Deleting Anexo Egsi with ID: {}", idAnexoEgsi);
        AnexoEgsi anexoEgsiFound = anexoEgsiRepository.findById(idAnexoEgsi)
            .orElseThrow(() -> new RuntimeException("Anexo Egsi not found with ID: " + idAnexoEgsi));
        anexoEgsiRepository.delete(anexoEgsiFound);
        log.info("Anexo Egsi with ID: {} has been deleted", idAnexoEgsi);
    }

}
