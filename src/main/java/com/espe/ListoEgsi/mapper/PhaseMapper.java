package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.espe.ListoEgsi.domain.dto.question.PhaseDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Phase;

@Mapper(componentModel = "spring")
public interface PhaseMapper {

    PhaseMapper INSTANCE = Mappers.getMapper(PhaseMapper.class);

    @Mapping(source = "process.idProcess", target = "idProcess")
    PhaseDTO toDTO(Phase phase);

    @Mapping(source = "idProcess", target = "process.idProcess")
    Phase toEntity(PhaseDTO phaseDTO);
}
