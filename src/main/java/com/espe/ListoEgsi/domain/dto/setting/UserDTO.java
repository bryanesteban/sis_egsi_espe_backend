package com.espe.ListoEgsi.domain.dto.setting;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private UUID id;
    private String name;
    private String lastname;
    private String cedula;
    private String username;
    private String password;
    private String roleName;


}
