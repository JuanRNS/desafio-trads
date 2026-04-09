package com.example.tradscorretora.infrastructure.repositories;

import com.example.tradscorretora.domain.entity.BusinessDealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessDealRepository extends JpaRepository<BusinessDealEntity, Long> {
}
