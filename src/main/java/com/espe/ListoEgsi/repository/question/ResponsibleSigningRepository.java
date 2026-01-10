package com.espe.ListoEgsi.repository.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.ResponsibleSigning;

@Repository
public interface ResponsibleSigningRepository extends JpaRepository<ResponsibleSigning, UUID> {
    
    List<ResponsibleSigning> findByPhase_IdPhase(UUID idPhase);
    
    List<ResponsibleSigning> findByStatusSign(String statusSign);
}
