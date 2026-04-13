package com.example.tradscorretora.domain.dto;

import java.sql.Timestamp;

public record VwTransitionsDTO(
        Long id,
        Long idNegocio,
        Long pipelineId,
        String stageFrom,
        String stageTo,
        Timestamp transition,
        Integer sortTo,
        Integer sortFrom
) {
}
