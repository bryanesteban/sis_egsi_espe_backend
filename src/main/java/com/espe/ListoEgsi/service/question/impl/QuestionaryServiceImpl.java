package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.QuestionaryDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Questionary;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.QuestionaryMapper;
import com.espe.ListoEgsi.repository.question.QuestionaryRepository;
import com.espe.ListoEgsi.service.question.QuestionaryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionaryServiceImpl implements QuestionaryService {

    private final QuestionaryRepository questionaryRepository;
    private final QuestionaryMapper questionaryMapper;

    @Override
    @Transactional
    public QuestionaryDTO createQuestionary(QuestionaryDTO questionaryDTO) {
        Questionary questionary = questionaryMapper.toEntity(questionaryDTO);
        Questionary savedQuestionary = questionaryRepository.save(questionary);
        return questionaryMapper.toDTO(savedQuestionary);
    }

    @Override
    @Transactional
    public QuestionaryDTO updateQuestionary(String id, QuestionaryDTO questionaryDTO) {
        Questionary existingQuestionary = questionaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Questionary not found with id: " + id));
        
        existingQuestionary.setQuestionaryName(questionaryDTO.getQuestionaryName());
        existingQuestionary.setDescription(questionaryDTO.getDescription());
        existingQuestionary.setPhase(questionaryDTO.getPhase());
        
        Questionary updatedQuestionary = questionaryRepository.save(existingQuestionary);
        return questionaryMapper.toDTO(updatedQuestionary);
    }

    @Override
    @Transactional
    public void deleteQuestionary(String id) {
        if (!questionaryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Questionary not found with id: " + id);
        }
        questionaryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionaryDTO getQuestionaryById(String id) {
        Questionary questionary = questionaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Questionary not found with id: " + id));
        return questionaryMapper.toDTO(questionary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionaryDTO> getAllQuestionaries() {
        return questionaryRepository.findAll().stream()
                .map(questionaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionaryDTO> getQuestionariesByPhase(String phase) {
        return questionaryRepository.findByPhase(phase).stream()
                .map(questionaryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
