package com.example.tradscorretora.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Long id;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "email", nullable = false)
    @JsonProperty("email")
    private String email;

    @Column(name = "papel", nullable = false)
    @JsonProperty("papel")
    private String papel;
}

