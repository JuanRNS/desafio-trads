package com.example.tradscorretora.config;

import com.example.tradscorretora.domain.entity.UserAcess;
import com.example.tradscorretora.infrastructure.repositories.UserAcessRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserAcessRepository userAcessRepository;

    public SecurityFilter(JwtConfig jwtConfig, UserAcessRepository userAcessRepository) {
        this.jwtConfig = jwtConfig;
        this.userAcessRepository = userAcessRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            String username = jwtConfig.validateToken(token);
            if (username != null) {
                UserAcess user = userAcessRepository.findByEmail(username).orElseThrow();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
