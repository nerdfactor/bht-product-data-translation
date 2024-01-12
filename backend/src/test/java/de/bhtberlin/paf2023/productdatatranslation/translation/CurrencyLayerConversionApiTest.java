package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyLayerConversionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CurrencyLayerConversionApiTest {

    @Autowired
    CurrencyLayerConversionStrategy api;


    @Test
    void shouldConvertCurrency() {
        double result = api.convertCurrency(1, "EUR", "USD");
        Assertions.assertNotEquals(1, result);
    }
}
