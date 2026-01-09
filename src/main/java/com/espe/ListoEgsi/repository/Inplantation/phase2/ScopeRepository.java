package com.espe.ListoEgsi.repository.Inplantation.phase2;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase2.ScopeEgsi;

public interface ScopeRepository extends JpaRepository<ScopeEgsi, UUID> {

    
}
