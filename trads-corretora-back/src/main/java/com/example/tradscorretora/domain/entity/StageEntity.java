package com.example.tradscorretora.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "stages",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_pipeline_status",
                columnNames = {"pipeline_id", "status_id"
        })
})
@Getter
@Setter
@NoArgsConstructor
public class StageEntity {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    @JsonProperty("id")
    private Long id;

    @JoinColumn(name = "pipeline_id",referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("pipeline_id")
    private PipelineEntity pipeline;

    @Column(name = "status_id", nullable = false)
    @JsonProperty("status_id")
    private String statusId;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "sort", nullable = false)
    @JsonProperty("sort")
    private Integer sort;

    @Column(name = "semantics", nullable = false, length = 10)
    @JsonProperty("semantics")
    private String semantics;
}

