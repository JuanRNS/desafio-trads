package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.UserAcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserAcessRepository extends JpaRepository<UserAcess, Long> {
    Optional<UserAcess> findByEmail(String email);
}
