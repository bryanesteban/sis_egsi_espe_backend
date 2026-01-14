package com.espe.ListoEgsi.mapper.phase;

import com.espe.ListoEgsi.domain.dto.phase.*;
import com.espe.ListoEgsi.domain.model.entity.phase.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EgsiPhaseMapper {

    // ============ ENTITY TO DTO ============

    public EgsiPhaseDTO toDTO(EgsiPhase entity) {
        if (entity == null) return null;
        
        List<EgsiSectionDTO> sortedSections = entity.getSections() != null 
            ? entity.getSections().stream()
                .sorted(Comparator.comparing(EgsiSection::getSectionOrder))
                .map(this::toDTO)
                .collect(Collectors.toList())
            : new ArrayList<>();
        
        return EgsiPhaseDTO.builder()
                .idPhase(entity.getIdPhase())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .order(entity.getPhaseOrder())
                .isActive(entity.getIsActive())
                .sections(sortedSections)
                .build();
    }

    public EgsiSectionDTO toDTO(EgsiSection entity) {
        if (entity == null) return null;
        
        List<EgsiQuestionDTO> sortedQuestions = entity.getQuestions() != null 
            ? entity.getQuestions().stream()
                .sorted(Comparator.comparing(EgsiQuestion::getQuestionOrder))
                .map(this::toDTO)
                .collect(Collectors.toList())
            : new ArrayList<>();
        
        return EgsiSectionDTO.builder()
                .idSection(entity.getIdSection())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .order(entity.getSectionOrder())
                .questions(sortedQuestions)
                .build();
    }

    public EgsiQuestionDTO toDTO(EgsiQuestion entity) {
        if (entity == null) return null;
        
        return EgsiQuestionDTO.builder()
                .idQuestion(entity.getIdQuestion())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .inputType(entity.getInputType())
                .required(entity.getIsRequired())
                .placeholder(entity.getPlaceholder())
                .maxLength(entity.getMaxLength())
                .tableConfig(entity.getTableConfig())
                .order(entity.getQuestionOrder())
                .build();
    }

    public List<EgsiPhaseDTO> toDTOList(List<EgsiPhase> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ============ DTO TO ENTITY ============

    public EgsiPhase toEntity(CreatePhaseRequestDTO dto) {
        if (dto == null) return null;
        
        EgsiPhase phase = EgsiPhase.builder()
                .idPhase(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .phaseOrder(dto.getOrder())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .sections(new HashSet<>())
                .build();

        if (dto.getSections() != null) {
            for (CreateSectionRequestDTO sectionDTO : dto.getSections()) {
                EgsiSection section = toEntity(sectionDTO);
                section.setPhase(phase);
                phase.getSections().add(section);
            }
        }

        return phase;
    }

    public EgsiSection toEntity(CreateSectionRequestDTO dto) {
        if (dto == null) return null;
        
        EgsiSection section = EgsiSection.builder()
                .idSection(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .sectionOrder(dto.getOrder())
                .questions(new HashSet<>())
                .build();

        if (dto.getQuestions() != null) {
            for (CreateQuestionRequestDTO questionDTO : dto.getQuestions()) {
                EgsiQuestion question = toEntity(questionDTO);
                question.setSection(section);
                section.getQuestions().add(question);
            }
        }

        return section;
    }

    public EgsiQuestion toEntity(CreateQuestionRequestDTO dto) {
        if (dto == null) return null;
        
        return EgsiQuestion.builder()
                .idQuestion(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .inputType(dto.getInputType())
                .isRequired(dto.getRequired())
                .placeholder(dto.getPlaceholder())
                .maxLength(dto.getMaxLength() != null ? dto.getMaxLength() : 1000)
                .tableConfig(dto.getTableConfig())
                .questionOrder(dto.getOrder())
                .build();
    }

    // ============ UPDATE ENTITY FROM DTO ============

    public void updateEntity(EgsiPhase entity, CreatePhaseRequestDTO dto) {
        if (entity == null || dto == null) return;
        
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPhaseOrder(dto.getOrder());
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
    }
}
