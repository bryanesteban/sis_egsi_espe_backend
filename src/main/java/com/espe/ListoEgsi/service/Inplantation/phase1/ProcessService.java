package com.espe.ListoEgsi.service.Inplantation.phase1;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase1.ProcessEgsiDTO;

public interface ProcessService {


    List<ProcessEgsiDTO> findAllProcess();
    ProcessEgsiDTO getProcess(UUID idProcess);
    ProcessEgsiDTO saveProcess(ProcessEgsiDTO processSubmit);
    ProcessEgsiDTO updateProcess(ProcessEgsiDTO processUpdate);
    void deleteProcess (UUID idProcess);
}
