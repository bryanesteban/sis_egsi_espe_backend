package com.espe.ListoEgsi.repository.phase;

import com.espe.ListoEgsi.domain.model.entity.phase.EgsiPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EgsiPhaseRepository extends JpaRepository<EgsiPhase, String> {
    
    List<EgsiPhase> findAllByOrderByPhaseOrderAsc();
    
    List<EgsiPhase> findByIsActiveTrueOrderByPhaseOrderAsc();
    
    @Query("SELECT p FROM EgsiPhase p LEFT JOIN FETCH p.sections s LEFT JOIN FETCH s.questions WHERE p.idPhase = :idPhase")
    Optional<EgsiPhase> findByIdWithSectionsAndQuestions(String idPhase);
    
    @Query("SELECT DISTINCT p FROM EgsiPhase p LEFT JOIN FETCH p.sections s LEFT JOIN FETCH s.questions ORDER BY p.phaseOrder ASC")
    List<EgsiPhase> findAllWithSectionsAndQuestions();
    
    @Query("SELECT DISTINCT p FROM EgsiPhase p LEFT JOIN FETCH p.sections s LEFT JOIN FETCH s.questions WHERE p.isActive = true ORDER BY p.phaseOrder ASC")
    List<EgsiPhase> findActiveWithSectionsAndQuestions();
    
    @Query("SELECT COUNT(p) FROM EgsiPhase p WHERE p.isActive = true")
    long countActivePhases();
    
    @Query("SELECT MAX(p.phaseOrder) FROM EgsiPhase p")
    Integer findMaxOrder();
    
    boolean existsByPhaseOrder(Integer phaseOrder);
}
