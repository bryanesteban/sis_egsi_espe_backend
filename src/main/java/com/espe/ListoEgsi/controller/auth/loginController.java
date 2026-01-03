package com.espe.ListoEgsi.controller.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.auth.ChangePasswordDTO;
import com.espe.ListoEgsi.domain.dto.auth.ChangeUsernameDTO;
import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.service.auth.LoginService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequestMapping("/login")
public class loginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Llamamos al servicio que creamos antes
            Map<String, String> response = loginService.authenticate(loginRequest);
            
            // Si todo sale bien, devolvemos el token y el tiempo de 1 hora
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            // Si el usuario o la clave (encriptada) no coinciden
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas", "mensaje", e.getMessage()));
        } catch (Exception e) {
            // Cualquier otro error técnico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor", "mensaje", e.getMessage()));
        }
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> chageUsername( @Valid @RequestBody ChangeUsernameDTO changeUsernameDTO, 
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
 

        if(changeUsernameDTO.getUsernameOld().equals(changeUsernameDTO.getUsernameNew()))
            throw new RuntimeException("The username can't be the before");
        
        try {
            
            UserDTO changedUsername = loginService.changeUsername(changeUsernameDTO);
            log.info("Successfully changing username: {}", changedUsername.getUsername());
            Map<String,String> response = new HashMap<>();
            response.put("Message:","Successfully changing username!");
            response.put("Username:",changedUsername.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error changing username: {}", changeUsernameDTO.getUsernameOld(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword( @Valid @RequestBody ChangePasswordDTO changePasswordDTO, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        if(changePasswordDTO.getPasswordOld().equals(changePasswordDTO.getPasswordNew()))
            throw new RuntimeException("The password can't be the before");

        try {
            
            loginService.changePassword(changePasswordDTO);
            log.info("Successfully changing password: {}", changePasswordDTO.getUsername());
            Map<String,String> response = new HashMap<>();
            response.put("Message","Successfully changing password!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error changing password: {}", changePasswordDTO.getUsername(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    
    

}
