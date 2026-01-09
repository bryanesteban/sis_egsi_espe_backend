package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase3.PlanOfComunicationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase3.PlanOfComunication;

@Mapper(componentModel = "spring")
public interface PlanOfComunicationMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    PlanOfComunicationDTO toDTO(PlanOfComunication plan);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    PlanOfComunication toEntity(PlanOfComunicationDTO dto);
    
}
