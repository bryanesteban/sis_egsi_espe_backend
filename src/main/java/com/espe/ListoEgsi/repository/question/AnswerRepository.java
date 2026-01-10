package com.espe.ListoEgsi.repository.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    
    List<Answer> findByPhase_IdPhase(UUID idPhase);
    
    List<Answer> findByQuestion_IdQuestion(UUID idQuestion);
    
    List<Answer> findByPhase_IdPhaseAndQuestion_IdQuestion(UUID idPhase, UUID idQuestion);
}
