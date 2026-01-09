package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase9.RiskTreatmentPlanDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase9.RiskTreatmentPlan;

@Mapper(componentModel = "spring")
public interface RiskTreatmentPlanMapper {

     @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    RiskTreatmentPlanDTO toDTO(RiskTreatmentPlan riskTreatmentPlan);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    RiskTreatmentPlan toEntity(RiskTreatmentPlanDTO dto);


}
