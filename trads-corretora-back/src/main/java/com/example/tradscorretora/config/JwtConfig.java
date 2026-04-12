package com.example.tradscorretora.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tradscorretora.domain.entity.UserAcess;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtConfig {
        @Value("${jwt.secret.key}")
        private String SECRET;

    @PostConstruct
    public void init() {
        if (SECRET == null || SECRET.isEmpty()) {
            System.err.println("ERRO CRÍTICO: Chave JWT não foi carregada!");
        } else {
            System.out.println("JWT Secret carregada com sucesso.");
        }
    }

        public String generateToken(UserAcess userAcess) {
            try{
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                return JWT.create()
                        .withSubject(userAcess.getEmail())
                        .withExpiresAt(getExpirationTime())
                        .withClaim("role", userAcess.getRole().name())
                        .sign(algorithm);
            }catch(JWTCreationException exception){
                throw new RuntimeException("Error creating JWT token", exception);
            }
        }

        public String validateToken(String token) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                return JWT.require(algorithm)
                        .build()
                        .verify(token)
                        .getSubject();
            } catch (JWTVerificationException exception) {
                return null;
            }
        }

        private Instant getExpirationTime() {
            return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
        }
}
