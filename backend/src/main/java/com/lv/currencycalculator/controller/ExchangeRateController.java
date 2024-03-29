package com.lv.currencycalculator.controller;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/getRate")
    public List<ExchangeRate> getAllCurrencies(@RequestParam String rateName, @RequestParam String dateFrom, @RequestParam String dateTo) {
        return exchangeRateService.getRateByNameAndPeriod(rateName, dateFrom, dateTo);
    }

    @GetMapping("/calculate")
    public BigDecimal calculateInForeignCurrency(@RequestParam String currencyCode, @RequestParam String date, @RequestParam String amount) {
        return exchangeRateService.calculateAmount(amount, date, currencyCode);
    }
}
