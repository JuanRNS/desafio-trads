package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwGain;
import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwGainRepository extends JpaRepository<VwGain, Long> {

    List<VwGain> findByIdNegocio(Long idNegocio);

}
