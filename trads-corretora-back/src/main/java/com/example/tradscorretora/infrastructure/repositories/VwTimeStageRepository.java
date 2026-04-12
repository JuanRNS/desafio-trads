package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwTimeStage;
import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwTimeStageRepository extends JpaRepository<VwTimeStage, Long> {

    List<VwTimeStage> findByIdNegocio(Long idNegocio);

}
