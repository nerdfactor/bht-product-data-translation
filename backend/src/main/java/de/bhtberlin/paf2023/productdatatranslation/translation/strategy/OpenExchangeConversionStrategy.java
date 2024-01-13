package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * External Api strategy for currency conversion using the OpenExchange Api.
 */
@RequiredArgsConstructor
@Component
public class OpenExchangeConversionStrategy implements CurrencyConversionStrategy {

    private final Map<String, Double> rates = new HashMap<>();
    private long lastCacheUpdate = 0;

    private static final String API_URL = "https://open.er-api.com/v6/latest/%s";


    private final ObjectMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, @NotNull String from, @NotNull String to) {
        if (!from.equalsIgnoreCase(AppConfig.DEFAULT_CURRENCY)) {
            throw new TranslationException("Currency conversion only works from default currency");
        }
        // check if last cache update is older than 24 hours. External Api only
        // updates once a day.
        if (System.currentTimeMillis() - lastCacheUpdate > 1000 * 60 * 60 * 24) {
            this.cacheExchangeRates();
        }
        String conversionKey = (from + to).toUpperCase();
        if (rates.containsKey(conversionKey)) {
            return currency * rates.get(conversionKey);
        }
        return currency;
    }

    private void cacheExchangeRates() {
        String uri = API_URL.formatted(AppConfig.DEFAULT_CURRENCY);

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(uri, String.class);

        try {
            JsonNode root = this.mapper.readTree(json);
            JsonNode quotes = root.path("rates");
            quotes.fields().forEachRemaining(stringJsonNodeEntry -> {
                String currency = stringJsonNodeEntry.getKey();
                double value = stringJsonNodeEntry.getValue().asDouble();
                this.rates.put(AppConfig.DEFAULT_CURRENCY + currency.toUpperCase(), value);
            });
        } catch (JsonProcessingException e) {
            // ignore this cache update
        }
        this.lastCacheUpdate = System.currentTimeMillis();
    }
}
