package com.espe.ListoEgsi.service.question;

import java.util.List;

import com.espe.ListoEgsi.domain.dto.question.QuestionaryDTO;

public interface QuestionaryService {
    
    QuestionaryDTO getQuestionaryById(String id);
    
    List<QuestionaryDTO> getAllQuestionaries();
    
    List<QuestionaryDTO> getQuestionariesByPhase(String phase);
}
