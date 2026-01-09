package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase2.ScopeEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase2.ScopeEgsi;


@Mapper(componentModel = "spring")
public interface ScopeMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    ScopeEgsiDTO toDTO(ScopeEgsi scope);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    ScopeEgsi toEntity(ScopeEgsiDTO dto);

}
