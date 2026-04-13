package com.example.tradscorretora.infrastructure.controllers;

import com.example.tradscorretora.domain.dto.JwtResponseDTO;
import com.example.tradscorretora.domain.dto.UserRequestDTO;
import com.example.tradscorretora.infrastructure.services.UserAcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAcessService userAcessService;

    public AuthController(UserAcessService userAcessService) {
        this.userAcessService = userAcessService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody UserRequestDTO userAcess) {
        String token = userAcessService.authenticate(userAcess);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
