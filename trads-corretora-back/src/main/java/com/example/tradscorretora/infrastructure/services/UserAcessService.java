package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.config.JwtConfig;
import com.example.tradscorretora.domain.dto.UserRequestDTO;
import com.example.tradscorretora.domain.entity.UserAcess;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAcessService {

    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    public UserAcessService(JwtConfig jwtConfig, AuthenticationManager authenticationManager) {
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    public String authenticate(UserRequestDTO userRequestDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userRequestDTO.email(),
                userRequestDTO.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        UserAcess userAcess = (UserAcess) authentication.getPrincipal();
        if (userAcess == null) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtConfig.generateToken(userAcess);
    }

}
