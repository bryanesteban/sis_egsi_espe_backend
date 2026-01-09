package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.AnexoEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.AnexoEgsi;

@Mapper(componentModel = "spring")
public interface AnexoEgsiMapper {

    @Mappings({
        @Mapping(source = "declaration.idDeclaration", target = "idDeclaration"),
    })
    AnexoEgsiDTO toDTO(AnexoEgsi anexoEgsi);

    @Mappings({
        @Mapping(target = "declaration", ignore = true)
    })
    AnexoEgsi toEntity(AnexoEgsiDTO dto);
    

}
