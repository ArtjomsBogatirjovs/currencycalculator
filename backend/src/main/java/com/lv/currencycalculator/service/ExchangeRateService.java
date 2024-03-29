package com.lv.currencycalculator.service;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.db.repository.ExchangeRateRepository;
import com.lv.currencycalculator.dto.CalculatedValueDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    @Value("${exchange.api.urlPeriod}")
    private String exchangeRateUrl;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateParserService exchangeRateParserService;

    public List<ExchangeRate> getRateByNameAndPeriod(String currency, String dateFrom, String dateTo) {
        List<ExchangeRate> result = new ArrayList<>();
        if (currency == null) {
            return result;
        }
        downloadRateInfo(currency, dateFrom, dateTo);
        result.addAll(exchangeRateRepository.findByCurrencyCodeAndDateBetween(currency, parseDate(dateFrom), parseDate(dateTo)));
        return result;
    }

    public List<ExchangeRate> getActualRates() {
        return exchangeRateRepository.findAllCurrenciesWithLatestDate();
    }

    public CalculatedValueDTO calculateAmount(String amountStr, String dateStr, String currency) {
        BigDecimal amount = new BigDecimal(amountStr);
        downloadRateInfo(currency, dateStr, dateStr);
        Optional<ExchangeRate> exchangeRateToExchange = exchangeRateRepository.findFirstByCurrencyCodeAndDateLessThanEqualOrderByDateDesc(currency, parseDate(dateStr));

        return exchangeRateToExchange.map(exchangeRate ->
                        CalculatedValueDTO.builder()
                                .convertedAmount(amount.multiply(exchangeRate.getRate()))
                                .usedRate(exchangeRate.getRate())
                                .currencyCode(currency)
                                .build())
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found for the given date and currency"));
    }


    public List<Object> getCurrencyCodes() {
        return exchangeRateRepository.getCurrencyCodes();
    }

    private LocalDate parseDate(String dateStr) {
        return Optional.ofNullable(dateStr)
                .filter(str -> !str.isEmpty())
                .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE))
                .orElse(LocalDate.now());
    }

    private String buildRequestUrl(String currencyCode, String fromDate, String toDate) {
        return String.format("%s&ccy=%s&dtFrom=%s&dtTo=%s", exchangeRateUrl, currencyCode, fromDate, toDate);
    }

    private void downloadRateInfo(String currency, String dateFrom, String dateTo) {
        try {
            String tempUrl = buildRequestUrl(currency, dateFrom, dateTo);
            exchangeRateParserService.parseAndSaveExchangeRates(tempUrl);
        } catch (Exception e) {
            log.error("Error download rates " + e.getMessage());
        }
    }

}
