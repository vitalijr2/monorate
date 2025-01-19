package io.github.vitalijr2.monorate.conversion;

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Convert money direct and inverse way.
 */
public interface Converter {

  /**
   * Direct conversion of money, e.g. <b>U.S. dollar</b> to <b>Ukrainian hryvnia</b>.
   *
   * @param amount   money amount
   * @param currency currency of money
   * @return converted amount
   * @throws UnsupportedCurrencyException if the currency is not supported by Monobank API.
   */
  default BigDecimal directConversion(@NotNull BigDecimal amount, @NotNull Currency currency)
      throws UnsupportedCurrencyException {
    return amount.multiply(getRate(currency))
        .setScale(Currency.getInstance("UAH").getDefaultFractionDigits(), HALF_EVEN);
  }

  /**
   * Inverse conversion of money, e.g. <b>Ukrainian hryvnia</b> to <b>U.S. dollar</b>.
   *
   * @param amount   money amount
   * @param currency currency of money
   * @return converted amount
   * @throws UnsupportedCurrencyException if the currency is not supported by Monobank API.
   */
  default BigDecimal inverseConversion(@NotNull BigDecimal amount, @NotNull Currency currency)
      throws UnsupportedCurrencyException {
    return amount.multiply(getInverseRate(currency)).setScale(currency.getDefaultFractionDigits(), HALF_EVEN);
  }

  /**
   * Get currency list.
   *
   * @return currency are supported by Monobank API
   */
  Set<Currency> getCurrencies();

  /**
   * Get inverse conversion rate for a currency.
   *
   * @param currency currency of money
   * @return conversion rate
   * @throws UnsupportedCurrencyException if the currency is not supported by Monobank API.
   */
  BigDecimal getInverseRate(@NotNull Currency currency) throws UnsupportedCurrencyException;

  /**
   * Get direct conversion rate for a currency.
   *
   * @param currency currency of money
   * @return conversion rate
   * @throws UnsupportedCurrencyException if the currency is not supported by Monobank API.
   */
  BigDecimal getRate(@NotNull Currency currency) throws UnsupportedCurrencyException;

}
