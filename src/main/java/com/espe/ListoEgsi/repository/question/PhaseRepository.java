package com.espe.ListoEgsi.repository.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.Phase;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, UUID> {
    
    List<Phase> findByProcess_IdProcess(UUID idProcess);
    
    List<Phase> findByStatus(String status);
    
    List<Phase> findByQuestionaryCode(String questionaryCode);
}
