package com.espe.ListoEgsi.repository.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    
    List<Question> findByQuestionary_IdQuestionary(String idQuestionary);
    
    List<Question> findByQuestionType(String questionType);
}
