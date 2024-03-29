package com.lv.currencycalculator.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class CalculatedValueDTO {
    private BigDecimal convertedAmount;
    private BigDecimal usedRate;
    private String currencyCode;
}
