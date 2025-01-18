package io.github.vitalijr2.monorate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
  void directConversion(String currencyCode, BigDecimal amount, BigDecimal expectedAmount) {
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
  void inverseConversion(String currencyCode, BigDecimal expectedAmount) {
    // given
    var currency = Currency.getInstance(currencyCode);

    doReturn(RATE).when(monorateService).getInverseRate(currency);

    // when
    var convertedAmount = monorateService.inverseConversion(BigDecimal.ONE, currency);

    // then
    assertEquals(expectedAmount, convertedAmount);
  }

}