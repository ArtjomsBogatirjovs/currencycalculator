package com.lv.currencycalculator.db.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "exchange_rate")
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "currencyCode")
    @NaturalId
    private String currencyCode;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "date")
    @NaturalId
    private LocalDate date;

}