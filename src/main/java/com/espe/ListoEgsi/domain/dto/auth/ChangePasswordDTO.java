package com.espe.ListoEgsi.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {
    private String username;
    private String passwordOld;
    private String passwordNew;
}
