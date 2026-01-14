package com.espe.ListoEgsi.repository.phase;

import com.espe.ListoEgsi.domain.model.entity.phase.EgsiSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EgsiSectionRepository extends JpaRepository<EgsiSection, String> {
    
    List<EgsiSection> findByPhaseIdPhaseOrderBySectionOrderAsc(String idPhase);
    
    @Query("SELECT COUNT(s) FROM EgsiSection s")
    long countAllSections();
    
    @Query("SELECT COUNT(s) FROM EgsiSection s WHERE s.phase.isActive = true")
    long countActiveSections();
    
    void deleteByPhaseIdPhase(String idPhase);
}
