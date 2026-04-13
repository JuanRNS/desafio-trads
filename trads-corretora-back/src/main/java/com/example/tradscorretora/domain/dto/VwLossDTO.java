package com.example.tradscorretora.domain.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record VwLossDTO(
        Long id,
        Long idNegocio,
        Long pipelineId,
        Timestamp lossDate,
        BigDecimal opportunity,
        String currencyId
) {
}
