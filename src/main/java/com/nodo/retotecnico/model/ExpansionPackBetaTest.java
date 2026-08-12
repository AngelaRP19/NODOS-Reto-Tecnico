package com.nodo.retotecnico.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "expansion_pack_beta_test")
@NoArgsConstructor
@AllArgsConstructor
public class ExpansionPackBetaTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "expansion_pack_id", nullable = false)
    private ExpansionPack expansionPack;

    @Enumerated(EnumType.STRING)
    private BetaTestStatus status = BetaTestStatus.EN_PRUEBA;

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}
