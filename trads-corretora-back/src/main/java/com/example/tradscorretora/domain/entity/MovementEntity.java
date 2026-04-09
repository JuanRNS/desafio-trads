package com.example.tradscorretora.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes")
@Getter
@Setter
@NoArgsConstructor
public class MovementEntity {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Long id;

    @JoinColumn(name = "id_negocio", referencedColumnName = "id", nullable = false)
    @JsonProperty("id_negocio")
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessDealEntity businessDeal;

    @Column(name = "type_id", nullable = false)
    @JsonProperty("type_id")
    private Integer typeId;

    @JoinColumn(name = "stage_id", referencedColumnName = "status_id", nullable = false)
    @JsonProperty("stage_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StageEntity stage;

    @JoinColumn(name = "pipeline_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("pipeline_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PipelineEntity pipeline;

    @Column(name = "created_time", nullable = false)
    @JsonProperty("created_time")
    private LocalDateTime createdTime;
}

