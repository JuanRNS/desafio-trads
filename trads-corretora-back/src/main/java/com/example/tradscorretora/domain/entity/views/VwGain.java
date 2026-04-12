package com.example.tradscorretora.domain.entity.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "vw_fechamento_ganho")
@Getter
@NoArgsConstructor
@Immutable
public class VwGain {

    @Id
    @Column(name = "id_movimentacao")
    private Long id;
    @Column(name = "id_negocio")
    private Long idNegocio;
    @Column(name = "pipeline_id")
    private Long pipelineId;
    @Column(name = "data_ganho")
    private Timestamp winDate;
    @Column(name = "opportunity")
    private BigDecimal opportunity;
    @Column(name = "currency_id")
    private String currencyId;
    @Column(name = "assigned_user_id")
    private Long userId;
}
