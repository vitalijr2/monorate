package io.github.vitalijr2.monorate.service;

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface MonorateService {

  default BigDecimal directConversion(@NotNull BigDecimal amount, @NotNull Currency currency)
      throws UnsupportedCurrencyException {
    return amount.multiply(getRate(currency))
        .setScale(Currency.getInstance("UAH").getDefaultFractionDigits(), HALF_EVEN);
  }

  default BigDecimal inverseConversion(@NotNull BigDecimal amount, @NotNull Currency currency)
      throws UnsupportedCurrencyException {
    return amount.multiply(getInverseRate(currency)).setScale(currency.getDefaultFractionDigits(), HALF_EVEN);
  }

  Set<Currency> getCurrencies();

  BigDecimal getInverseRate(@NotNull Currency currency) throws UnsupportedCurrencyException;

  BigDecimal getRate(@NotNull Currency currency) throws UnsupportedCurrencyException;

}
