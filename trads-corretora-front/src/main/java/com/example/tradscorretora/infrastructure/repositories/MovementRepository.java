package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<MovementEntity, Long> {
}
