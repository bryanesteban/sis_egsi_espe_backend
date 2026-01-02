package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.domain.model.entity.setting.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
                @Mapping(source = "idUser", target = "id"),
                @Mapping(source = "ci", target = "cedula"),
                @Mapping(source = "rol", target = "roleName"),
                @Mapping(target = "password", constant = "")
        })
    UserDTO toDTO(User user);
    
    @Mappings({
                @Mapping(source = "id", target = "idUser" ),
                @Mapping(source = "cedula", target = "ci" ),
                @Mapping(source = "roleName", target = "rol")
        })
        User toEntity(UserDTO dto);

}
