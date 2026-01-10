package com.espe.ListoEgsi.service.question.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Question;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.mapper.QuestionMapper;
import com.espe.ListoEgsi.repository.question.QuestionRepository;
import com.espe.ListoEgsi.service.question.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toDTO(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(Integer id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        
        existingQuestion.setDescription(questionDTO.getDescription());
        existingQuestion.setQuestionType(questionDTO.getQuestionType());
        existingQuestion.setQuestionJson(questionDTO.getQuestionJson());
        
        Question updatedQuestion = questionRepository.save(existingQuestion);
        return questionMapper.toDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return questionMapper.toDTO(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByQuestionary(String idQuestionary) {
        return questionRepository.findByQuestionary_IdQuestionary(idQuestionary).stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
