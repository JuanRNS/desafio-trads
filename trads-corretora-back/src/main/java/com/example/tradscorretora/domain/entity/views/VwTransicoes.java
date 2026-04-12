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
@Table(name = "vw_transicoes")
@Immutable
@Getter
@NoArgsConstructor
public class VwTransicoes {
    @Id
    @Column(name = "id_movimentacao")
    private Long id;
    @Column(name = "id_negocio")
    private Long idNegocio;
    @Column(name = "pipeline_id")
    private Long pipelineId;
    @Column(name = "stage_from")
    private String stageFrom;
    @Column(name = "stage_to")
    private String stageTo;
    @Column(name = "t_transicao")
    private Timestamp transicao;
    @Column(name = "sort_from")
    private Integer sortFrom;
    @Column(name = "sort_to")
    private Integer sortTo;
}
