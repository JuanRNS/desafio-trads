package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.PipelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineRepository extends JpaRepository<PipelineEntity, Long> {
}
