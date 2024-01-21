package de.bhtberlin.paf2023.productdatatranslation.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyLayerConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.OpenExchangeConversionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class OpenExchangeConversionApiTest {

    OpenExchangeConversionStrategy api = new OpenExchangeConversionStrategy(new ObjectMapper());


    @Test
    void shouldConvertCurrency() {
        double result = api.convertCurrency(1, "EUR", "USD");
        Assertions.assertNotEquals(1, result);
    }
}
