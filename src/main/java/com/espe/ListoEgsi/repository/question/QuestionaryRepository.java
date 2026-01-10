package com.espe.ListoEgsi.repository.question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.Questionary;

@Repository
public interface QuestionaryRepository extends JpaRepository<Questionary, String> {
    
    List<Questionary> findByPhase(String phase);
    
    Questionary findByQuestionaryName(String questionaryName);
}
