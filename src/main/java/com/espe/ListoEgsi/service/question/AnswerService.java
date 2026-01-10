package com.espe.ListoEgsi.service.question;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;

public interface AnswerService {
    
    AnswerDTO createAnswer(AnswerDTO answerDTO);
    
    AnswerDTO updateAnswer(UUID id, AnswerDTO answerDTO);
    
    void deleteAnswer(UUID id);
    
    AnswerDTO getAnswerById(UUID id);
    
    List<AnswerDTO> getAllAnswers();
    
    List<AnswerDTO> getAnswersByPhase(UUID idPhase);
    
    List<AnswerDTO> getAnswersByQuestion(UUID idQuestion);
}
