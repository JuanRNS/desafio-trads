package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VwTransicoesRepository extends JpaRepository<VwTransicoes, Long> {

    @Query("SELECT v FROM VwTransicoes v WHERE " +
            "(:start IS NULL OR v.transicao >= :start) AND " +
            "(:end IS NULL OR v.transicao <= :end)")
    Page<VwTransicoes> findAllByStartBetween(Pageable pageable, LocalDateTime start, LocalDateTime end);

    @Query(
            "SELECT v FROM VwTransicoes v WHERE v.userId = :userId " +
                    "AND (:start IS NULL OR v.transicao >= :start) " +
                    "AND (:end IS NULL OR v.transicao <= :end)"
    )
    Page<VwTransicoes> findByUserIdAndStartBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);


}
