package ru.lkodos.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FullExchangeRate {

    private Integer id;
    private Integer baseCurrencyId;
    private String baseCurrencyCode;
    private String baseCurrencyName;
    private String baseCurrencySign;
    private Integer targetCurrencyId;
    private String targetCurrencyCode;
    private String targetCurrencyName;
    private String targetCurrencySign;
    private BigDecimal rate;
}