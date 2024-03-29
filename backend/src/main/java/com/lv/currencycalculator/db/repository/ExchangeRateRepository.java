package com.lv.currencycalculator.db.repository;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query("select e from ExchangeRate e where e.currencyCode = ?1 and e.date between ?2 and ?3")
    List<ExchangeRate> findByCurrencyCodeAndDateBetween(String currencyCode, LocalDate dateStart, LocalDate dateEnd);

    Optional<ExchangeRate> findByDateAndCurrencyCode(LocalDate date, String currencyCode);
}