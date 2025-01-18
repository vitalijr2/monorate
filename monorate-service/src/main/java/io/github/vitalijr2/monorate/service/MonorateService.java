package io.github.vitalijr2.monorate.service;

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

public interface MonorateService {

  default BigDecimal getConvertedAmount(BigDecimal amount, Currency currency, boolean inverse) {
    var rate = inverse ? getInverseRate(currency) : getRate(currency);
    var scale = inverse ? Currency.getInstance("UAH").getDefaultFractionDigits() : currency.getDefaultFractionDigits();

    return amount.multiply(rate).setScale(scale, HALF_EVEN);
  }

  Set<Currency> getCurrencies();

  BigDecimal getInverseRate(Currency currency);

  BigDecimal getRate(Currency currency);

}
