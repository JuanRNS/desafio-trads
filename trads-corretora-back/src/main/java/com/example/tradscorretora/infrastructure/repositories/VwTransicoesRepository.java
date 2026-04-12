package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwTransicoesRepository extends JpaRepository<VwTransicoes, Long> {


    List<VwTransicoes> findByIdNegocio(Long idNegocio);
}
