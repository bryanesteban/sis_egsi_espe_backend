package com.espe.ListoEgsi.service.auth.impl;

import com.espe.ListoEgsi.auth.JwtUtil;
import com.espe.ListoEgsi.domain.dto.auth.ChangePasswordDTO;
import com.espe.ListoEgsi.domain.dto.auth.ChangeUsernameDTO;
import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.domain.model.entity.setting.User;
import com.espe.ListoEgsi.mapper.UserMapper;
import com.espe.ListoEgsi.repository.UserRepository;
import com.espe.ListoEgsi.service.auth.LoginService;

import org.mapstruct.control.MappingControl.Use;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImp implements LoginService {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public Map<String, String> authenticate(LoginRequestDTO loginRequest) {
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Clave incorrecta");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("rolename", user.getRol());
        response.put("token", token);

        return response;
    }

    public UserDTO changeUsername(ChangeUsernameDTO changeUsernameDTO){

        User user = userRepository.findByUsername(changeUsernameDTO.getUsernameOld())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(changeUsernameDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Clave incorrecta");
        }

        user.setUsername(changeUsernameDTO.getUsernameNew());
        User userchanged = userRepository.save(user);
        return userMapper.toDTO(userchanged);

    }

    public UserDTO changePassword(ChangePasswordDTO changePasswordDTO){

        User user = userRepository.findByUsername(changePasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(changePasswordDTO.getPasswordOld(), user.getPassword())) {
            throw new RuntimeException("Clave incorrecta");
        }

        String passwordEncriptada = passwordEncoder.encode(changePasswordDTO.getPasswordNew());
        user.setPassword(passwordEncriptada);
        User userchanged = userRepository.save(user);
        return userMapper.toDTO(userchanged);

        
    }

    







}