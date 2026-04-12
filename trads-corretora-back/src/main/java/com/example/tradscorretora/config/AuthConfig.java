package com.example.tradscorretora.config;

import com.example.tradscorretora.infrastructure.repositories.UserAcessRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Configuration
public class AuthConfig implements UserDetailsService {

    private final UserAcessRepository userAcessRepository;

    public AuthConfig(UserAcessRepository userAcessRepository) {
        this.userAcessRepository = userAcessRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAcessRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
