package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwLoss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VwLossRepository extends JpaRepository<VwLoss, Long> {

    List<VwLoss> findByIdNegocio(Long idNegocio);
    @Query("SELECT v FROM VwLoss v WHERE " +
            "(:startDate IS NULL OR v.lossDate >= :startDate) AND " +
            "(:endDate IS NULL OR v.lossDate <= :endDate)")
    Page<VwLoss> findAllByDate(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT v FROM VwLoss v WHERE v.userId = :userId AND " +
            "(:startDate IS NULL OR v.lossDate >= :startDate) AND " +
            "(:endDate IS NULL OR v.lossDate <= :endDate)")
    Page<VwLoss> findByUserIdAndDate(Long userId, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);


}
