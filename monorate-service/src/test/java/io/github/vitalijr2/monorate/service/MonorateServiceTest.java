package io.github.vitalijr2.monorate.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class MonorateServiceTest {

  private static final BigDecimal RATE = new BigDecimal("1.23456789");

  @Spy
  private MonorateService monorateService;

  @DisplayName("Direct conversion")
  @ParameterizedTest(name = "{1} => {2}")
  @CsvSource({"USD,1,1.23", "ISK,10,12.35", "OMR,100,123.46"})
  void directConversion(String currencyCode, BigDecimal amount, BigDecimal expectedAmount)
      throws UnsupportedCurrencyException {
    // given
    var currency = Currency.getInstance(currencyCode);

    doReturn(RATE).when(monorateService).getRate(currency);

    // when
    var convertedAmount = monorateService.directConversion(amount, currency);

    // then
    assertEquals(expectedAmount, convertedAmount);
  }

  @DisplayName("Inverse conversion")
  @ParameterizedTest(name = "{0}")
  @CsvSource({"USD,1.23", "ISK,1", "OMR,1.235"})
  void inverseConversion(String currencyCode, BigDecimal expectedAmount) throws UnsupportedCurrencyException {
    // given
    var currency = Currency.getInstance(currencyCode);

    doReturn(RATE).when(monorateService).getInverseRate(currency);

    // when
    var convertedAmount = monorateService.inverseConversion(BigDecimal.ONE, currency);

    // then
    assertEquals(expectedAmount, convertedAmount);
  }

  @DisplayName("Unsupported currency")
  @Test
  void unsupportedCurrency() throws UnsupportedCurrencyException {
    // given
    var amount = BigDecimal.ONE;
    var currency = Currency.getInstance("USD");

    doThrow(new UnsupportedCurrencyException("inverse conversion")).when(monorateService)
        .getInverseRate(isA(Currency.class));
    doThrow(new UnsupportedCurrencyException("direct conversion")).when(monorateService).getRate(isA(Currency.class));

    // when
    var exceptionOnDirectConversion = assertThrows(UnsupportedCurrencyException.class,
        () -> monorateService.directConversion(amount, currency));
    var exceptionOnInverseConversion = assertThrows(UnsupportedCurrencyException.class,
        () -> monorateService.inverseConversion(amount, currency));

    assertAll(() -> assertEquals("direct conversion", exceptionOnDirectConversion.getMessage()),
        () -> assertEquals("inverse conversion", exceptionOnInverseConversion.getMessage()));
  }

}