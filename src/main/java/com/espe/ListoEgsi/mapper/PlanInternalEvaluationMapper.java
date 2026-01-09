package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase4.PlanInternalEvaluationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase4.PlanInternalEvaluation;

@Mapper(componentModel = "spring")
public interface PlanInternalEvaluationMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    PlanInternalEvaluationDTO toDTO(PlanInternalEvaluation plan);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    PlanInternalEvaluation toEntity(PlanInternalEvaluationDTO planDTO);
}
