package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwGain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VwGainRepository extends JpaRepository<VwGain, Long> {

        List<VwGain> findByIdNegocio(Long idNegocio, Pageable pageable);

        @Query("SELECT v FROM VwGain v WHERE " +
                        "(:startDate IS NULL OR v.winDate >= :startDate) AND " +
                        "(:endDate IS NULL OR v.winDate <= :endDate)")
        Page<VwGain> findAllByDate(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT v FROM VwGain v WHERE v.userId = :userId AND " +
                        "(:startDate IS NULL OR v.winDate >= :startDate) AND " +
                        "(:endDate IS NULL OR v.winDate <= :endDate)")
        Page<VwGain> findByUserIdAndDate(Long userId, Pageable pageable, LocalDateTime startDate,
                        LocalDateTime endDate);

}
