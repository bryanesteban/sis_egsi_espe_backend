package com.espe.ListoEgsi.service.setting.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserModDTO;
import com.espe.ListoEgsi.domain.model.entity.setting.User;
import com.espe.ListoEgsi.mapper.UserMapper;
import com.espe.ListoEgsi.repository.UserRepository;
import com.espe.ListoEgsi.service.setting.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.espe.ListoEgsi.exception.CustomExceptionOnly;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> usersFounds = userRepository.findAll();
        return usersFounds.
                stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get user answer by ID
     */
    @Override
    public UserDTO getUser(UUID idUser) {
        log.info("Fetching User with ID: {}", idUser);
        return userRepository.findByIdAndNotDeleted(idUser.toString())
               .map(userMapper::toDTO)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + idUser));

    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
       String passwordEncriptada = passwordEncoder.encode(userDTO.getPassword());
       User savedUser = new User();

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent())
            {
                logger.warn("Username Found, you can't use that username"); 
                throw new CustomExceptionOnly("Username Found, you can't use the username: "+userDTO.getUsername(), HttpStatus.CONFLICT);
            }

            userDTO.setPassword(passwordEncriptada);
            User user = userMapper.toEntity(userDTO);
            user.setDeleted(false);
            savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public UserModDTO updateUser(UserModDTO submitDTO) {
        
        User userModified = new User();

            User userFound =  userRepository.findById(submitDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + submitDTO.getId()));

               if(submitDTO.getName() != null) userFound.setName(submitDTO.getName());
               if(submitDTO.getLastname() != null)  userFound.setLastname(submitDTO.getLastname());
               if(submitDTO.getCedula() != null)  userFound.setCi(submitDTO.getCedula());
               if(submitDTO.getRoleName() != null)  userFound.setRol(submitDTO.getRoleName());
               if(submitDTO.getCedula() != null)  userFound.setCi(submitDTO.getCedula());

                userModified = userRepository.save(userFound);
                log.info("Successfully submitted user with ID: {}", userModified.getIdUser());
            

        return mapToDTO(userModified);

    }

    @Transactional
    public void softDeleteUser(UUID idUser) {
        log.info("Soft deleting UserAnswer with ID: {}", idUser);
        userRepository.findByIdAndNotDeleted(idUser.toString()).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + idUser));
        userRepository.softDeleteById(idUser.toString());
        log.info("Successfully soft deleted User with ID: {}", idUser);

    }


    public UserModDTO mapToDTO(User user){
        return UserModDTO.builder()
                .id(user.getIdUser())
                .name(user.getName())
                .lastname(user.getLastname())
                .cedula(user.getCi())
                .username(user.getUsername())
                .build();

    }


}
