package com.espe.ListoEgsi.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hash")
public class HashGeneratorController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/generate")
    public Map<String, String> generateHash(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String hash = passwordEncoder.encode(password);
        
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", String.valueOf(passwordEncoder.matches(password, hash)));
        
        return response;
    }
    
    @PostMapping("/verify")
    public Map<String, String> verifyHash(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String hash = request.get("hash");
        
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", String.valueOf(passwordEncoder.matches(password, hash)));
        
        return response;
    }
}
