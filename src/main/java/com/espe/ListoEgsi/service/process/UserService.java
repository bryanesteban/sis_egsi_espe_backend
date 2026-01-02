package com.espe.ListoEgsi.service.process;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserModDTO;

public interface UserService {
    
    List<UserDTO> findAllUsers();
    UserDTO getUser(UUID idUser);
    UserDTO saveUser(UserDTO user);
    UserModDTO updateUser(UserModDTO userMod);
    void softDeleteUser(UUID idUser);

}
