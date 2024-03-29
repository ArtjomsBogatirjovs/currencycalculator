package com.lv.currencycalculator.controller;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.dto.CalculatedValueDTO;
import com.lv.currencycalculator.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/getRate")
    public List<ExchangeRate> getCurrencyByPeriod(@RequestParam String rateName, @RequestParam String dateFrom, @RequestParam String dateTo) {
        return exchangeRateService.getRateByNameAndPeriod(rateName, dateFrom, dateTo);
    }

    @GetMapping("/calculate")
    public CalculatedValueDTO calculateInForeignCurrency(@RequestParam String currencyCode, @RequestParam String date, @RequestParam String amount) {
        return exchangeRateService.calculateAmount(amount, date, currencyCode);
    }

    @GetMapping("/getActualRates")
    public List<ExchangeRate> getAllCurrencies() {
        return exchangeRateService.getActualRates();
    }

    @GetMapping("/getAvailableCurrencies")
    public List<Object> getAvailableCurrencies() {
        return exchangeRateService.getCurrencyCodes();
    }
}
