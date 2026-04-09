package com.example.tradscorretora.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "negocios", check = {
        @CheckConstraint(
                name = "check_closed",
                constraint = "(closed = 0 AND closedate IS NULL) OR (closed = 1 AND closedate IS NOT NULL)"
        )
})
@Getter
@Setter
@NoArgsConstructor
public class BusinessDealEntity {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id_negocio")
    private Long id;

    @Column(name = "titulo", nullable = false)
    @JsonProperty("titulo")
    private String titulo;

    @JoinColumn(name = "pipeline_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PipelineEntity pipeline;

    @JoinColumn(name = "stage_id", referencedColumnName = "status_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private StageEntity stage;

    @Column(name = "stage_semantics", nullable = false, length = 10)
    @JsonProperty("stage_semantics")
    private String stageSemantics;

    @Column(name = "opportunity", nullable = false, precision = 19, scale = 2)
    @JsonProperty("opportunity")
    private BigDecimal opportunity;

    @Column(name = "currency_id", nullable = false, length = 10)
    @JsonProperty("currency_id")
    private String currencyId;

    @Column(name = "probability", nullable = false)
    @JsonProperty("probability")
    private Integer probability;

    @JoinColumn(name = "assigned_user_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("assigned_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity assignedUser;

    @JoinColumn(name = "created_user_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("created_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity createdUser;

    @Column(name = "date_create", nullable = false)
    @JsonProperty("date_create")
    private LocalDateTime dateCreate;

    @Column(name = "date_modify", nullable = false)
    @JsonProperty("date_modify")
    private LocalDateTime dateModify;

    @Column(name = "moved_time", nullable = false)
    @JsonProperty("moved_time")
    private LocalDateTime movedTime;

    @Column(name = "closed", nullable = false)
    @JsonProperty("closed")
    private Boolean closed;

    @Column(name = "closedate")
    @JsonProperty("closedate")
    private LocalDate closeDate;

    @Column(name = "utm_source")
    @JsonProperty("utm_source")
    private String utmSource;

    @Column(name = "utm_medium")
    @JsonProperty("utm_medium")
    private String utmMedium;

    @Column(name = "utm_campaign")
    @JsonProperty("utm_campaign")
    private String utmCampaign;

    @Column(name = "comments")
    @JsonProperty("comments")
    private String comments;

    @Column(name = "custom_fields", columnDefinition = "TEXT")
    @JsonProperty("custom_fields")
    private String customFields;
}

