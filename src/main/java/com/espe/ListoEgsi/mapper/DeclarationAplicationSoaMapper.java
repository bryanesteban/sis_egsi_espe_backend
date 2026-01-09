package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.DeclarationAplicationSoaDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.DeclarationAplicationSoa;

@Mapper(componentModel = "spring")
public interface DeclarationAplicationSoaMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    DeclarationAplicationSoaDTO toDTO(DeclarationAplicationSoa declarationAplicationSoa);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    DeclarationAplicationSoa toEntity(DeclarationAplicationSoaDTO dto);
    

}