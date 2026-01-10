package com.espe.ListoEgsi.service.question;

import java.util.List;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;

public interface QuestionService {
    
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    
    QuestionDTO updateQuestion(Integer id, QuestionDTO questionDTO);
    
    void deleteQuestion(Integer id);
    
    QuestionDTO getQuestionById(Integer id);
    
    List<QuestionDTO> getAllQuestions();
    
    List<QuestionDTO> getQuestionsByQuestionary(String idQuestionary);
}
