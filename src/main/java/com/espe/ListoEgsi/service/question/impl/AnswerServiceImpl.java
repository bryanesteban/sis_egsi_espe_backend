package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Answer;
import com.espe.ListoEgsi.domain.model.entity.question.Phase;
import com.espe.ListoEgsi.domain.model.entity.question.Question;
import com.espe.ListoEgsi.domain.model.entity.question.Questionary;
import com.espe.ListoEgsi.enums.StatusEnum;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.AnswerMapper;
import com.espe.ListoEgsi.repository.question.AnswerRepository;
import com.espe.ListoEgsi.repository.question.PhaseRepository;
import com.espe.ListoEgsi.repository.question.QuestionRepository;
import com.espe.ListoEgsi.repository.question.QuestionaryRepository;
import com.espe.ListoEgsi.service.question.AnswerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerMapper answerMapper;
    
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    PhaseRepository phaseRepository;

    @Autowired
    QuestionaryRepository questionaryRepository;

    @Override
    @Transactional
    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        Answer answer = answerMapper.toEntity(answerDTO);
        Answer savedAnswer = answerRepository.save(answer);
        return answerMapper.toDTO(savedAnswer);
    }

    @Override
    @Transactional
    public AnswerDTO updateAnswer(UUID id, AnswerDTO answerDTO) {
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id));
        
        existingAnswer.setAnswerText(answerDTO.getAnswerText());
        existingAnswer.setAnswerType(answerDTO.getAnswerType());
        existingAnswer.setAnswerStatus(answerDTO.getAnswerStatus());
        existingAnswer.setUpdatedAt(answerDTO.getUpdatedAt());
        
        Answer updatedAnswer = answerRepository.save(existingAnswer);
        return answerMapper.toDTO(updatedAnswer);
    }

    @Override
    @Transactional
    public void deleteAnswer(UUID id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Answer not found with id: " + id);
        }
        answerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerDTO getAnswerById(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id));
        return answerMapper.toDTO(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerDTO> getAllAnswers() {
        return answerRepository.findAll().stream()
                .map(answerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerDTO> getAnswersByPhase(UUID idPhase) {
        return answerRepository.findByPhase_IdPhase(idPhase).stream()
                .map(answerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerDTO> getAnswersByQuestion(Integer idQuestion) {
        return answerRepository.findByQuestion_IdQuestion(idQuestion).stream()
                .map(answerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnswerDTO> createAnswersByPhase(UUID idPhase, String idQuestionary) {

    Phase phase = phaseRepository.findById(idPhase)
            .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id: " + idPhase));

    Questionary questionary = questionaryRepository.findById(idQuestionary)
            .orElseThrow(() -> new ResourceNotFoundException("Questionary not found with id: " + idQuestionary));

    List<Question> questions = questionRepository.findByQuestionary(questionary);

    List<Answer> answers = questions.stream()
            .map(question -> {
                Answer answer = new Answer();
                answer.setPhase(phase);
                answer.setQuestion(question);
                answer.setAnswerText(""); // Respuesta vacía inicial
                answer.setAnswerType(question.getQuestionType());
                answer.setAnswerStatus(StatusEnum.ACTIVE.getStateName()); // o "NOT_ANSWERED"
                answer.setCreatedAt(java.time.LocalDateTime.now().toString());
                answer.setUpdatedAt(java.time.LocalDateTime.now().toString());
                return answer;
            })
            .collect(Collectors.toList());

  
    List<Answer> savedAnswers = answerRepository.saveAll(answers);

    return savedAnswers.stream()
            .map(answerMapper::toDTO)
            .collect(Collectors.toList());
}
}
