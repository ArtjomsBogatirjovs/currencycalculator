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

    @Query("SELECT er FROM ExchangeRate er WHERE er.date IN (SELECT MAX(er2.date) FROM ExchangeRate er2 WHERE er2.currencyCode = er.currencyCode GROUP BY er2.currencyCode)")
    List<ExchangeRate> findAllCurrenciesWithLatestDate();

    @Query("SELECT er.currencyCode FROM ExchangeRate er WHERE er.date IN (SELECT MAX(er2.date) FROM ExchangeRate er2 WHERE er2.currencyCode = er.currencyCode GROUP BY er2.currencyCode)")
    List<Object> getCurrencyCodes();

    Optional<ExchangeRate> findFirstByCurrencyCodeAndDateLessThanEqualOrderByDateDesc(String currencyCode, LocalDate date);


}