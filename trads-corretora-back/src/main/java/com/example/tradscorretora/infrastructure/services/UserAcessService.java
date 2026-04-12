package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.config.JwtConfig;
import com.example.tradscorretora.domain.dto.JwtResponse;
import com.example.tradscorretora.domain.dto.UserRequest;
import com.example.tradscorretora.domain.entity.UserAcess;
import com.example.tradscorretora.infrastructure.repositories.UserAcessRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAcessService {

    private final UserAcessRepository userAcessRepository;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    public UserAcessService(UserAcessRepository userAcessRepository, JwtConfig jwtConfig, AuthenticationManager authenticationManager) {
        this.userAcessRepository = userAcessRepository;
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    public String authenticate(UserRequest userRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        UserAcess userAcess = (UserAcess) authentication.getPrincipal();
        if (userAcess == null) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtConfig.generateToken(userAcess);
    }

}
