package com.example.tradscorretora.domain.entity.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.sql.Timestamp;

@Entity
@Table(name = "vw_tempo_em_etapa")
@Immutable
@Getter
@NoArgsConstructor
public class VwTimeStage {
        @Id
        @Column(name = "id_movimentacao")
        private Long id;
        @Column(name = "id_negocio")
        private Long idNegocio;
        @Column(name = "stage_id")
        private String stageId;
        @Column(name = "pipeline_id")
        private Long pipelineId;
        @Column(name = "inicio")
        private Timestamp Start;
        @Column(name = "fim")
        private Timestamp end;
        @Column(name = "dias_na_etapa")
        private Timestamp daysInStage;
}
