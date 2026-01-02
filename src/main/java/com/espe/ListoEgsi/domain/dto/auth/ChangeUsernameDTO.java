package com.espe.ListoEgsi.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUsernameDTO {
    private String usernameOld;
    private String usernameNew;
    private String password;

}
