package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    QuestionaryRepository questionaryRepository;
    @Autowired
    QuestionaryMapper questionaryMapper;

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
