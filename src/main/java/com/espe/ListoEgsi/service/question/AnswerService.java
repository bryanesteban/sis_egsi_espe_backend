package com.espe.ListoEgsi.service.question;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;

public interface AnswerService {

    AnswerDTO updateAnswer(AnswerDTO answerDTO);
    
    AnswerDTO getAnswerById(UUID id);
    
    List<AnswerDTO> getAllAnswers();
    
    List<AnswerDTO> getAnswersByPhase(UUID idPhase);
    
    List<AnswerDTO> getAnswersByQuestion(Integer idQuestion);

    List<AnswerDTO> createAnswersByPhase(UUID idPhase, String idQuestionary);

    Boolean validateAnswersCompletedByPhase(UUID idPhase);
    
}
