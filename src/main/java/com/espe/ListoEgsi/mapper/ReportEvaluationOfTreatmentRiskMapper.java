package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase7.ReportEvaluationOfTreatmentRiskDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase7.ReportEvaluationOfTreatmentRisk;



@Mapper(componentModel = "spring")
public interface ReportEvaluationOfTreatmentRiskMapper {

     @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    ReportEvaluationOfTreatmentRiskDTO toDTO(ReportEvaluationOfTreatmentRisk ReportEvaluationOfTreatment);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    ReportEvaluationOfTreatmentRisk toEntity(ReportEvaluationOfTreatmentRiskDTO dto);


}
