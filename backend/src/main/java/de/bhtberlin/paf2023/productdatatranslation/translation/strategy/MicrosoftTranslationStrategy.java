package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Set;

/**
 * External translation Api using the Microsoft Translation Api.
 */
public class MicrosoftTranslationStrategy implements ExternalTranslationApiStrategy {

    private final String apiKey;

    private final String apiRegion;

    private final ObjectMapper mapper;

    private final static String apiUrl = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&from=%s&to=%s";

    public MicrosoftTranslationStrategy(String apiKey, String apiRegion, ObjectMapper mapper) {
        this.apiKey = apiKey;
        this.apiRegion = apiRegion;
        this.mapper = mapper;
    }

    /**
     * Supported locales.
     */
    private static final Set<String> supportedLocales = Set.of("af", "sq", "am", "ar", "hy", "as", "az", "bn", "ba", "eu", "bho", "brx", "bs", "bg", "yue", "ca", "lzh", "zh-Hans", "zh-Hant", "sn", "hr", "cs", "da", "prs", "dv", "doi", "nl", "en", "et", "fo", "fj", "fil", "fi", "fr", "fr-ca", "gl", "ka", "de", "el", "gu", "ht", "ha", "he", "hi", "mww", "hu", "is", "ig", "id", "ikt", "iu", "iu-Latn", "ga", "it", "ja", "kn", "ks", "kk", "km", "rw", "tlh-Latn", "tlh-Piqd", "gom", "ko", "ku", "kmr", "ky", "lo", "lv", "lt", "ln", "dsb", "lug", "mk", "mai", "mg", "ms", "ml", "mt", "mi", "mr", "mn-Cyrl", "mn-Mong", "my", "ne", "nb", "nya", "or", "ps", "fa", "pl", "pt", "pt-pt", "pa", "otq", "ro", "run", "ru", "sm", "sr-Cyrl", "sr-Latn", "st", "nso", "tn", "sd", "si", "sk", "sl", "so", "es", "sw", "sv", "ty", "ta", "tt", "te", "th", "bo", "ti", "to", "tr", "tk", "uk", "hsb", "ur", "ug", "uz", "vi", "cy", "xh", "yo", "yua", "zu");

    /**
     * {@inheritDoc}
     */
    public @NotNull Set<String> getSupportedLocales() {
        return supportedLocales;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(@Nullable String text, @NotNull String from, @NotNull String to) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (isNotSupportedLocale(from) || isNotSupportedLocale(to)) {
            return text;
        }

        Set<TranslationRequest> requestObjects = Collections.singleton(new TranslationRequest(text));

        RestTemplate restTemplate = new RestTemplate();

        String uri = apiUrl.formatted(from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Ocp-Apim-Subscription-Key", this.apiKey);
        headers.add("Ocp-Apim-Subscription-Region", this.apiRegion);

        String json = restTemplate.postForObject(uri, new HttpEntity<>(requestObjects, headers), String.class);

        try {
            JsonNode root = this.mapper.readTree(json);
            return root.path(0).path("translations").path(0).path("text").asText(text);
        } catch (JsonProcessingException e) {
            return text;
        }
    }

    @AllArgsConstructor
    public static class TranslationRequest {

        @JsonProperty("Text")
        private String text;
    }

}
