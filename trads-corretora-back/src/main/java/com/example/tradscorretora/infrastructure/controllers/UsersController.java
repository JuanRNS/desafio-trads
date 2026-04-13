package com.example.tradscorretora.infrastructure.controllers;

import com.example.tradscorretora.domain.dto.UserListDTO;
import com.example.tradscorretora.infrastructure.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    public List<UserListDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
