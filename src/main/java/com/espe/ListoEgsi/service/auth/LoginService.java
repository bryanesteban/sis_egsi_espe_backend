package com.espe.ListoEgsi.service.auth;

import java.util.Map;

import com.espe.ListoEgsi.domain.dto.auth.ChangePasswordDTO;
import com.espe.ListoEgsi.domain.dto.auth.ChangeUsernameDTO;
import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserDTO;

public interface LoginService  {

    Map<String, String> authenticate(LoginRequestDTO loginRequest);
    UserDTO changeUsername(ChangeUsernameDTO changeUsernameDTO);
    UserDTO changePassword(ChangePasswordDTO changePasswordDTO);
    Map<String, String> refreshToken(String token);

}
