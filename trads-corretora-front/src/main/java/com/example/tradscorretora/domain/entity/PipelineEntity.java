package com.example.tradscorretora.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "pipelines")
@Getter
@Setter
@NoArgsConstructor
public class PipelineEntity {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Long id;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "sort", nullable = false)
    @JsonProperty("sort")
    private Integer sort;

    @Column(name = "descricao")
    @JsonProperty("descricao")
    private String descricao;

    @OneToMany(mappedBy = "pipeline", cascade = CascadeType.ALL)
    private List<StageEntity> etapas;
}

