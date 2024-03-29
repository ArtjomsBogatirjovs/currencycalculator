package com.lv.currencycalculator.service;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.db.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private static final String EUR_CURRENCY = "EUR";


    public List<ExchangeRate> getRateByNameAndPeriod(String currency, String dateFrom, String dateTo) {
        if (currency == null) {
            return new ArrayList<>();
        }
        return exchangeRateRepository.findByCurrencyCodeAndDateBetween(currency, parseDate(dateFrom), parseDate(dateTo));
    }

    public BigDecimal calculateAmount(String amountStr, String dateStr, String currency) {
        BigDecimal amount = new BigDecimal(amountStr);
        LocalDate date = parseDate(dateStr);
        Optional<ExchangeRate> exchangeRateToExchange = exchangeRateRepository.findByDateAndCurrencyCode(date, currency);
        return exchangeRateToExchange.map(exchangeRate -> amount.multiply(exchangeRate.getRate()))
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found for the given date and currency"));
    }

    private LocalDate parseDate(String dateStr) {
        return Optional.ofNullable(dateStr)
                .filter(str -> !str.isEmpty())
                .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE))
                .orElse(LocalDate.now());
    }
}
