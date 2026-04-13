package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.views.VwLossConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwLossConversionRepository extends JpaRepository<VwLossConversion, Long>
{
    List<VwLossConversion> findByUserId(Long userId);

}
