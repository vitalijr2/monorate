package io.github.vitalijr2.monorate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.Currency;
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

  @ParameterizedTest(name = "{0}, inverse: {2}")
  @CsvSource({"USD,1.23,false", "ISK,1,false", "OMR,1.235,false",
      "USD,1.23,true", "ISK,1.23,true", "OMR,1.23,true"})
  void getConvertedAmount(String currencyCode, BigDecimal expectedAmount, boolean inverse) {
    // given
    var currency = Currency.getInstance(currencyCode);

    if (inverse) {
      doReturn(RATE).when(monorateService).getInverseRate(currency);
    } else {
      doReturn(RATE).when(monorateService).getRate(currency);
    }

    // when
    var amount = monorateService.getConvertedAmount(BigDecimal.ONE, currency, inverse);

    // then
    assertEquals(expectedAmount, amount);
  }

}