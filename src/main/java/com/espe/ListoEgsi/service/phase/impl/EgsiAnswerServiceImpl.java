package com.espe.ListoEgsi.service.phase.impl;

import com.espe.ListoEgsi.domain.dto.phase.EgsiAnswerDTO;
import com.espe.ListoEgsi.domain.dto.phase.SaveAnswersRequestDTO;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiAnswer;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiQuestion;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.repository.phase.EgsiAnswerRepository;
import com.espe.ListoEgsi.repository.phase.EgsiQuestionRepository;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.service.phase.EgsiAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de respuestas EGSI.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgsiAnswerServiceImpl implements EgsiAnswerService {

    private final EgsiAnswerRepository answerRepository;
    private final EgsiQuestionRepository questionRepository;
    private final ProcessRepository processRepository;

    @Override
    @Transactional
    public List<EgsiAnswerDTO> saveAnswers(SaveAnswersRequestDTO request, String username) {
        log.info("Guardando {} respuestas para proceso {} y fase {}", 
                request.getAnswers() != null ? request.getAnswers().size() : 0,
                request.getIdProcess(), 
                request.getIdPhase());

        UUID processUUID = UUID.fromString(request.getIdProcess());
        ProcessEgsi process = processRepository.findById(processUUID)
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado: " + request.getIdProcess()));

        List<EgsiAnswerDTO> savedAnswers = new ArrayList<>();

        if (request.getAnswers() != null) {
            for (SaveAnswersRequestDTO.AnswerItem item : request.getAnswers()) {
                if (item.getIdQuestion() == null || item.getIdQuestion().isEmpty()) {
                    continue;
                }

                EgsiQuestion question = questionRepository.findById(item.getIdQuestion())
                        .orElse(null);
                
                if (question == null) {
                    log.warn("Pregunta no encontrada: {}", item.getIdQuestion());
                    continue;
                }

                // Buscar respuesta existente o crear nueva
                EgsiAnswer answer = answerRepository.findByProcessAndQuestion(
                        processUUID, item.getIdQuestion())
                        .orElse(new EgsiAnswer());

                boolean isNew = answer.getIdAnswer() == null;
                
                answer.setProcess(process);
                answer.setQuestion(question);
                answer.setIdPhase(request.getIdPhase());
                answer.setAnswerValue(item.getAnswerValue());
                
                // Determinar estado basado en si hay contenido
                String status = (item.getAnswerValue() != null && !item.getAnswerValue().trim().isEmpty()) 
                        ? "IN_PROGRESS" : "PENDING";
                answer.setStatus(status);

                if (isNew) {
                    answer.setCreatedBy(username);
                }
                answer.setUpdatedBy(username);

                EgsiAnswer savedAnswer = answerRepository.save(answer);
                savedAnswers.add(convertToDTO(savedAnswer));
            }
        }

        log.info("Se guardaron {} respuestas exitosamente", savedAnswers.size());
        return savedAnswers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgsiAnswerDTO> getAnswersByProcess(String idProcess) {
        UUID processUUID = UUID.fromString(idProcess);
        return answerRepository.findByProcessIdProcess(processUUID).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgsiAnswerDTO> getAnswersByProcessAndPhase(String idProcess, String idPhase) {
        UUID processUUID = UUID.fromString(idProcess);
        return answerRepository.findByProcessAndPhase(processUUID, idPhase).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getAnswersMapByProcessAndPhase(String idProcess, String idPhase) {
        UUID processUUID = UUID.fromString(idProcess);
        List<EgsiAnswer> answers = answerRepository.findByProcessAndPhase(processUUID, idPhase);
        Map<String, String> answersMap = new HashMap<>();
        
        for (EgsiAnswer answer : answers) {
            if (answer.getQuestion() != null) {
                answersMap.put(answer.getQuestion().getIdQuestion(), answer.getAnswerValue());
            }
        }
        
        return answersMap;
    }

    @Override
    @Transactional(readOnly = true)
    public int getPhaseProgress(String idProcess, String idPhase) {
        UUID processUUID = UUID.fromString(idProcess);
        long total = answerRepository.countByProcessAndPhase(processUUID, idPhase);
        if (total == 0) return 0;
        
        long completed = answerRepository.countCompletedByProcessAndPhase(processUUID, idPhase);
        return (int) Math.round((completed * 100.0) / total);
    }

    @Override
    @Transactional
    public void deleteAnswer(String idAnswer) {
        answerRepository.deleteById(idAnswer);
    }

    @Override
    @Transactional
    public void deleteAnswersByProcess(String idProcess) {
        UUID processUUID = UUID.fromString(idProcess);
        answerRepository.deleteByProcessIdProcess(processUUID);
    }

    private EgsiAnswerDTO convertToDTO(EgsiAnswer answer) {
        return EgsiAnswerDTO.builder()
                .idAnswer(answer.getIdAnswer())
                .idProcess(answer.getProcess() != null ? answer.getProcess().getIdProcess().toString() : null)
                .idQuestion(answer.getQuestion() != null ? answer.getQuestion().getIdQuestion() : null)
                .idPhase(answer.getIdPhase())
                .answerValue(answer.getAnswerValue())
                .status(answer.getStatus())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .createdBy(answer.getCreatedBy())
                .updatedBy(answer.getUpdatedBy())
                .build();
    }
}
