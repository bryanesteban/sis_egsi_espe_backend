package com.espe.ListoEgsi.service.question;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;

public interface QuestionService {
    
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    
    QuestionDTO updateQuestion(UUID id, QuestionDTO questionDTO);
    
    void deleteQuestion(UUID id);
    
    QuestionDTO getQuestionById(UUID id);
    
    List<QuestionDTO> getAllQuestions();
    
    List<QuestionDTO> getQuestionsByQuestionary(String idQuestionary);
}
