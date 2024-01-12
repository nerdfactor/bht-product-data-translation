package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Currency conversion strategy that just returns the original value without
 * converting it. This strategy could be used during testing or development.
 */
public class CurrencyLayerConversionStrategy implements CurrencyConversionStrategy {

    private final Map<String, Double> rates = new HashMap<>();
    private long lastCacheUpdate = 0;

    private static final String apiURL = "http://api.currencylayer.com/live?access_key=%s&source=%s";

    private final String apiKey;

    private final ObjectMapper mapper;

    public CurrencyLayerConversionStrategy(String apiKey, ObjectMapper mapper) {
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

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
        String uri = apiURL.formatted(apiKey, AppConfig.DEFAULT_CURRENCY);

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(uri, String.class);

        try {
            JsonNode root = this.mapper.readTree(json);
            JsonNode quotes = root.path("quotes");
            quotes.fields().forEachRemaining(stringJsonNodeEntry -> {
                String currency = stringJsonNodeEntry.getKey().substring(3);
                double value = stringJsonNodeEntry.getValue().asDouble();
                this.rates.put(AppConfig.DEFAULT_CURRENCY + currency.toUpperCase(), value);
            });
        } catch (JsonProcessingException e) {
            // ignore this cache update
        }
        this.lastCacheUpdate = System.currentTimeMillis();
    }
}
