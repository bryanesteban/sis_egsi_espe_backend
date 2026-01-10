package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Answer;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.AnswerMapper;
import com.espe.ListoEgsi.repository.question.AnswerRepository;
import com.espe.ListoEgsi.service.question.AnswerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

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
}
