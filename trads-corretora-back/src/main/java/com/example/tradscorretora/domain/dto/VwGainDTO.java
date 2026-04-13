package com.example.tradscorretora.domain.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record VwGainDTO(
        Long id,
        Long idNegocio,
        Long pipelineId,
        Timestamp winDate,
        BigDecimal opportunity,
        String currencyId
) {
}
