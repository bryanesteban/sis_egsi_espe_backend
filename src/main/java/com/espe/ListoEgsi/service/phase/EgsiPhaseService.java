package com.espe.ListoEgsi.service.phase;

import com.espe.ListoEgsi.domain.dto.phase.*;
import com.espe.ListoEgsi.domain.model.entity.phase.*;
import com.espe.ListoEgsi.mapper.phase.EgsiPhaseMapper;
import com.espe.ListoEgsi.repository.phase.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class EgsiPhaseService {

    private final EgsiPhaseRepository phaseRepository;
    private final EgsiSectionRepository sectionRepository;
    private final EgsiQuestionRepository questionRepository;
    private final EgsiPhaseMapper mapper;

    // ============ GET ALL PHASES ============
    public EgsiPhasesResponseDTO getAllPhases() {
        log.info("Obteniendo todas las fases EGSI");
        
        List<EgsiPhase> phases = phaseRepository.findAllWithSectionsAndQuestions();
        List<EgsiPhaseDTO> phaseDTOs = mapper.toDTOList(phases);
        
        int totalSections = phases.stream()
                .mapToInt(p -> p.getSections() != null ? p.getSections().size() : 0)
                .sum();
        
        int totalQuestions = phases.stream()
                .flatMap(p -> p.getSections() != null ? p.getSections().stream() : Stream.empty())
                .mapToInt(s -> s.getQuestions() != null ? s.getQuestions().size() : 0)
                .sum();
        
        int activePhases = (int) phases.stream().filter(EgsiPhase::getIsActive).count();
        
        return EgsiPhasesResponseDTO.builder()
                .totalPhases(phases.size())
                .activePhases(activePhases)
                .totalSections(totalSections)
                .totalQuestions(totalQuestions)
                .egsiPhases(phaseDTOs)
                .build();
    }

    // ============ GET ACTIVE PHASES ONLY ============
    public EgsiPhasesResponseDTO getActivePhases() {
        log.info("Obteniendo fases EGSI activas");
        
        List<EgsiPhase> phases = phaseRepository.findActiveWithSectionsAndQuestions();
        List<EgsiPhaseDTO> phaseDTOs = mapper.toDTOList(phases);
        
        int totalSections = phases.stream()
                .mapToInt(p -> p.getSections() != null ? p.getSections().size() : 0)
                .sum();
        
        int totalQuestions = phases.stream()
                .flatMap(p -> p.getSections() != null ? p.getSections().stream() : Stream.empty())
                .mapToInt(s -> s.getQuestions() != null ? s.getQuestions().size() : 0)
                .sum();
        
        return EgsiPhasesResponseDTO.builder()
                .totalPhases(phases.size())
                .activePhases(phases.size())
                .totalSections(totalSections)
                .totalQuestions(totalQuestions)
                .egsiPhases(phaseDTOs)
                .build();
    }

    // ============ GET PHASE BY ID ============
    public EgsiPhaseDTO getPhaseById(String idPhase) {
        log.info("Obteniendo fase con ID: {}", idPhase);
        
        return phaseRepository.findByIdWithSectionsAndQuestions(idPhase)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Fase no encontrada con ID: " + idPhase));
    }

    // ============ CREATE SINGLE PHASE ============
    @Transactional
    public EgsiPhaseDTO createPhase(CreatePhaseRequestDTO request) {
        log.info("Creando nueva fase: {}", request.getTitle());
        
        EgsiPhase phase = mapper.toEntity(request);
        EgsiPhase savedPhase = phaseRepository.save(phase);
        
        return mapper.toDTO(savedPhase);
    }

    // ============ SAVE ALL PHASES (REPLACE ALL) ============
    @Transactional
    public EgsiPhasesResponseDTO saveAllPhases(SaveAllPhasesRequestDTO request) {
        log.info("Guardando {} fases EGSI", request.getPhases().size());
        
        // Eliminar todas las fases existentes
        phaseRepository.deleteAll();
        
        // Crear las nuevas fases
        List<EgsiPhase> newPhases = new ArrayList<>();
        for (CreatePhaseRequestDTO phaseDTO : request.getPhases()) {
            EgsiPhase phase = mapper.toEntity(phaseDTO);
            newPhases.add(phase);
        }
        
        // Guardar todas las fases
        List<EgsiPhase> savedPhases = phaseRepository.saveAll(newPhases);
        
        log.info("Se guardaron {} fases correctamente", savedPhases.size());
        
        return getAllPhases();
    }

    // ============ UPDATE PHASE ============
    @Transactional
    public EgsiPhaseDTO updatePhase(String idPhase, CreatePhaseRequestDTO request) {
        log.info("Actualizando fase con ID: {}", idPhase);
        
        EgsiPhase existingPhase = phaseRepository.findById(idPhase)
                .orElseThrow(() -> new RuntimeException("Fase no encontrada con ID: " + idPhase));
        
        // Actualizar campos básicos
        existingPhase.setTitle(request.getTitle());
        existingPhase.setDescription(request.getDescription());
        existingPhase.setPhaseOrder(request.getOrder());
        if (request.getIsActive() != null) {
            existingPhase.setIsActive(request.getIsActive());
        }
        
        // Limpiar secciones existentes y agregar nuevas
        existingPhase.getSections().clear();
        
        if (request.getSections() != null) {
            for (CreateSectionRequestDTO sectionDTO : request.getSections()) {
                EgsiSection section = mapper.toEntity(sectionDTO);
                section.setPhase(existingPhase);
                existingPhase.getSections().add(section);
            }
        }
        
        EgsiPhase savedPhase = phaseRepository.save(existingPhase);
        return mapper.toDTO(savedPhase);
    }

    // ============ TOGGLE PHASE ACTIVE ============
    @Transactional
    public EgsiPhaseDTO togglePhaseActive(String idPhase) {
        log.info("Cambiando estado de fase con ID: {}", idPhase);
        
        EgsiPhase phase = phaseRepository.findById(idPhase)
                .orElseThrow(() -> new RuntimeException("Fase no encontrada con ID: " + idPhase));
        
        phase.setIsActive(!phase.getIsActive());
        EgsiPhase savedPhase = phaseRepository.save(phase);
        
        log.info("Fase {} ahora está {}", idPhase, savedPhase.getIsActive() ? "activa" : "inactiva");
        
        return mapper.toDTO(savedPhase);
    }

    // ============ DELETE PHASE ============
    @Transactional
    public void deletePhase(String idPhase) {
        log.info("Eliminando fase con ID: {}", idPhase);
        
        if (!phaseRepository.existsById(idPhase)) {
            throw new RuntimeException("Fase no encontrada con ID: " + idPhase);
        }
        
        phaseRepository.deleteById(idPhase);
        log.info("Fase {} eliminada correctamente", idPhase);
    }

    // ============ ADD SECTION TO PHASE ============
    @Transactional
    public EgsiSectionDTO addSectionToPhase(String idPhase, CreateSectionRequestDTO request) {
        log.info("Agregando sección a fase: {}", idPhase);
        
        EgsiPhase phase = phaseRepository.findById(idPhase)
                .orElseThrow(() -> new RuntimeException("Fase no encontrada con ID: " + idPhase));
        
        EgsiSection section = mapper.toEntity(request);
        section.setPhase(phase);
        
        EgsiSection savedSection = sectionRepository.save(section);
        return mapper.toDTO(savedSection);
    }

    // ============ ADD QUESTION TO SECTION ============
    @Transactional
    public EgsiQuestionDTO addQuestionToSection(String idSection, CreateQuestionRequestDTO request) {
        log.info("Agregando pregunta a sección: {}", idSection);
        
        EgsiSection section = sectionRepository.findById(idSection)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada con ID: " + idSection));
        
        EgsiQuestion question = mapper.toEntity(request);
        question.setSection(section);
        
        EgsiQuestion savedQuestion = questionRepository.save(question);
        return mapper.toDTO(savedQuestion);
    }

    // ============ DELETE SECTION ============
    @Transactional
    public void deleteSection(String idSection) {
        log.info("Eliminando sección con ID: {}", idSection);
        
        if (!sectionRepository.existsById(idSection)) {
            throw new RuntimeException("Sección no encontrada con ID: " + idSection);
        }
        
        sectionRepository.deleteById(idSection);
    }

    // ============ DELETE QUESTION ============
    @Transactional
    public void deleteQuestion(String idQuestion) {
        log.info("Eliminando pregunta con ID: {}", idQuestion);
        
        if (!questionRepository.existsById(idQuestion)) {
            throw new RuntimeException("Pregunta no encontrada con ID: " + idQuestion);
        }
        
        questionRepository.deleteById(idQuestion);
    }

    // ============ GET STATISTICS ============
    public EgsiPhasesResponseDTO getStatistics() {
        long totalPhases = phaseRepository.count();
        long activePhases = phaseRepository.countActivePhases();
        long totalSections = sectionRepository.countAllSections();
        long totalQuestions = questionRepository.countAllQuestions();
        
        return EgsiPhasesResponseDTO.builder()
                .totalPhases((int) totalPhases)
                .activePhases((int) activePhases)
                .totalSections((int) totalSections)
                .totalQuestions((int) totalQuestions)
                .build();
    }
}
