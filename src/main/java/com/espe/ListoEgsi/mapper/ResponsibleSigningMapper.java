package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.espe.ListoEgsi.domain.dto.question.ResponsibleSigningDTO;
import com.espe.ListoEgsi.domain.model.entity.question.ResponsibleSigning;

@Mapper(componentModel = "spring")
public interface ResponsibleSigningMapper {

    ResponsibleSigningMapper INSTANCE = Mappers.getMapper(ResponsibleSigningMapper.class);

    @Mapping(source = "phase.idPhase", target = "idPhase")
    ResponsibleSigningDTO toDTO(ResponsibleSigning responsibleSigning);

    @Mapping(source = "idPhase", target = "phase.idPhase")
    ResponsibleSigning toEntity(ResponsibleSigningDTO responsibleSigningDTO);
}
