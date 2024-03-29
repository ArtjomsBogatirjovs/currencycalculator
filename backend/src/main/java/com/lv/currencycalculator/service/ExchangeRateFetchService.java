package com.lv.currencycalculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class ExchangeRateFetchService {
    @Value("${exchange.api.url}")
    private String exchangeRateUrl;

    private final ExchangeRateParserService exchangeRateParserService;

    @Scheduled(cron = "${exchange.rate.fetch.cron}")
    public void downloadLatestExchangeRates() {
        try {
            URI tempUri = new URI(exchangeRateUrl);
            URLConnection connection = tempUri.toURL().openConnection();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(connection.getInputStream());
            exchangeRateParserService.parseAndSaveExchangeRates(document);
        } catch (Exception e) {
            System.out.println("Error download rates " + e.getMessage());
        }
    }
}
