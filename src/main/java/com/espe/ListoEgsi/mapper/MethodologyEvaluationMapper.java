package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase6.MethodologyEvaluationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase6.MethodologyEvaluation;


@Mapper(componentModel = "spring")
public interface MethodologyEvaluationMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    MethodologyEvaluationDTO toDTO(MethodologyEvaluation methodologyEvaluation);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    MethodologyEvaluation toEntity(MethodologyEvaluationDTO dto);
    

}
