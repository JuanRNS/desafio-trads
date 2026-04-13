package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwTimeStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VwTimeStageRepository extends JpaRepository<VwTimeStage, Long> {

    List<VwTimeStage> findByIdNegocio(Long idNegocio);
    @Query("SELECT v FROM VwTimeStage v WHERE " +
            "(:start IS NULL OR v.end >= :start) AND " +
            "(:end IS NULL OR v.end <= :end)")
    Page<VwTimeStage> findAllByDate(Pageable pageable, LocalDateTime start, LocalDateTime end);
    @Query("SELECT v FROM VwTimeStage v WHERE v.userId = :userId AND " +
            "(:start IS NULL OR v.end >= :start) AND " +
            "(:end IS NULL OR v.end <= :end)")
    Page<VwTimeStage> findByUserIdByDate(Long userId, Pageable pageable, LocalDateTime start, LocalDateTime end);

}
