package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.domain.dto.UserListDTO;
import com.example.tradscorretora.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserListDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserListDTO(user.getId(), user.getNome()))
                .toList();
    }
}
