package com.espe.ListoEgsi.service.question;

import java.util.List;

import com.espe.ListoEgsi.domain.dto.question.QuestionaryDTO;

public interface QuestionaryService {
    
    QuestionaryDTO createQuestionary(QuestionaryDTO questionaryDTO);
    
    QuestionaryDTO updateQuestionary(String id, QuestionaryDTO questionaryDTO);
    
    void deleteQuestionary(String id);
    
    QuestionaryDTO getQuestionaryById(String id);
    
    List<QuestionaryDTO> getAllQuestionaries();
    
    List<QuestionaryDTO> getQuestionariesByPhase(String phase);
}
