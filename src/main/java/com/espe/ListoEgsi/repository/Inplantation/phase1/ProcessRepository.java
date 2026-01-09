package com.espe.ListoEgsi.repository.Inplantation.phase1;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.setting.User;


@Repository
public interface ProcessRepository extends JpaRepository<ProcessEgsi, UUID> {

    /**
     * find by id Process
     */
    User findByIdProcess(UUID idProcess);

}
