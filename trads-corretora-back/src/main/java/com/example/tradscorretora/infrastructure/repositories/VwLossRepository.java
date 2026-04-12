package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwLoss;
import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwLossRepository extends JpaRepository<VwLoss, Long> {

    List<VwLoss> findByIdNegocio(Long idNegocio);

}
