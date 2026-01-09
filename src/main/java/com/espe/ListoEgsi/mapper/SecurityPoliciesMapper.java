package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.Implantation.phase5.SecurityPoliciesDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase5.SecurityPolicies;

@Mapper(componentModel = "spring")
public interface SecurityPoliciesMapper {

    @Mappings({
        @Mapping(source = "process.idProcess", target = "idProcess"),
    })
    SecurityPoliciesDTO toDTO(SecurityPolicies securityPolicies);

    @Mappings({
        @Mapping(target = "process", ignore = true)
    })
    SecurityPolicies toEntity(SecurityPoliciesDTO dto);

}
