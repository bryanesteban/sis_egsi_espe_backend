package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;

import com.espe.ListoEgsi.domain.dto.Implantation.phase1.ProcessEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;



@Mapper(componentModel = "spring")
public interface ProcessEgsiMapper {

    ProcessEgsiDTO toDTO(ProcessEgsi process);
    ProcessEgsi toEntity(ProcessEgsiDTO dto);
}
