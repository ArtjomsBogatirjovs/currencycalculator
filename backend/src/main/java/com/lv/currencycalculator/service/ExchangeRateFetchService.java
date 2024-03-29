package com.lv.currencycalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateFetchService {
    @Value("${exchange.api.urlCurrent}")
    private String exchangeRateUrl;

    private final ExchangeRateParserService exchangeRateParserService;

    @Scheduled(cron = "${exchange.rate.fetch.cron}")
    public void downloadLatestExchangeRates() {
        try {
            exchangeRateParserService.parseAndSaveExchangeRates(exchangeRateUrl);
        } catch (Exception e) {
            log.error("Error download rates " + e.getMessage());
        }
    }
}
