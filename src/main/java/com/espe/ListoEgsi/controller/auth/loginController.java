package com.espe.ListoEgsi.controller.auth;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.auth.LoginRequestDTO;
import com.espe.ListoEgsi.service.auth.LoginService;




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
    public String chageUsername(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    

}
