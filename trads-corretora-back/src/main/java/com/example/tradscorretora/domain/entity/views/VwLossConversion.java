package com.example.tradscorretora.domain.entity.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_loss_conversion" )
@Getter
@NoArgsConstructor
@Immutable
public class VwLossConversion {

    @Id
    @Column(name = "id_movimentacao")
    private Long id;
    @Column(name = "stage_from")
    private String stageFrom;
    @Column(name = "status_id")
    private String statusId;
    @Column(name = "total_transicoes")
    private Long totalTransitions;
    @Column(name = "assigned_user_id")
    private Long userId;
}
