package com.example.tradscorretora.domain.dto;

import java.sql.Timestamp;

public record VwTimeStageDTO(
        Long id,
        Long idNegocio,
        Long pipelineId,
        String stageId,
        Timestamp start,
        Timestamp end,
        Long daysInStage
) {
}
