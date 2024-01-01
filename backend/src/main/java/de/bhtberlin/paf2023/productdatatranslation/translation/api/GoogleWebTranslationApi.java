package de.bhtberlin.paf2023.productdatatranslation.translation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * External translation Api using the unofficial Google Web Api.
 */
@Component
@RequiredArgsConstructor
public class GoogleWebTranslationApi implements ExternalTranslationApi {

    private static final String apiUrl = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";

    private final ObjectMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translate(@NotNull String text, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException {
        RestTemplate restTemplate = new RestTemplate();

        String uri = apiUrl.formatted(from, to, URLEncoder.encode(text, StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/121.0");

        String json = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
        try {
            JsonNode root = this.mapper.readTree(json);
            return root.path(0).path(0).path(0).asText(text);
        } catch (JsonProcessingException e) {
            throw new ExternalTranslationApiException("Exception during Google Web Api translation", e);
        }
    }
}
