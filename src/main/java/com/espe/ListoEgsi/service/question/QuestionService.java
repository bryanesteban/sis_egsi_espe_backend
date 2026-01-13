package com.espe.ListoEgsi.service.question;

import java.util.List;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;

public interface QuestionService {
    
    QuestionDTO getQuestionById(Integer id);
    
    List<QuestionDTO> getAllQuestions();
    
    List<QuestionDTO> getQuestionsByQuestionary(String idQuestionary);
}
