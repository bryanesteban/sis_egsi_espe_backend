package com.espe.ListoEgsi.repository.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    
    List<Answer> findByPhase_IdPhase(UUID idPhase);
    
    List<Answer> findByQuestion_IdQuestion(Integer idQuestion);
    
    List<Answer> findByPhase_IdPhaseAndQuestion_IdQuestion(UUID idPhase, Integer idQuestion);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN false ELSE true END FROM Answers a WHERE a.phase.idPhase = :idPhase AND a.answerStatus <> 'COMPLETED'")
    Boolean VerifyAnswersCompletedByPhase(@Param("idPhase") UUID idPhase);
}
