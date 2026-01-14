package com.espe.ListoEgsi.repository.phase;

import com.espe.ListoEgsi.domain.model.entity.phase.EgsiQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EgsiQuestionRepository extends JpaRepository<EgsiQuestion, String> {
    
    List<EgsiQuestion> findBySectionIdSectionOrderByQuestionOrderAsc(String idSection);
    
    @Query("SELECT COUNT(q) FROM EgsiQuestion q")
    long countAllQuestions();
    
    @Query("SELECT COUNT(q) FROM EgsiQuestion q WHERE q.section.phase.isActive = true")
    long countActiveQuestions();
    
    void deleteBySectionIdSection(String idSection);
}
