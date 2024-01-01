package de.bhtberlin.paf2023.productdatatranslation.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.translation.BaseTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.MultiStrategyTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.ExternalTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.GoogleWebTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.ExternalApiTextTranslationStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.FakeCurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.FakeMeasurementConversionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class TranslationConfig {

    final AppConfig appConfig;

    @Bean
    @Primary
    public BaseTranslator getTranslator() {
        // todo: get from configuration.
        // todo: somehow change during runtime?
        return MultiStrategyTranslator.builder()
                .withTextStrategy(new ExternalApiTextTranslationStrategy(getExternalTranslationApi()))
                .withCurrencyStrategy(new FakeCurrencyConversionStrategy())
                .withMeasurementStrategy(new FakeMeasurementConversionStrategy())
                .build();
    }

    @Bean
    @Primary
    public ExternalTranslationApi getExternalTranslationApi() {
        // todo: get api from configuration.
        // todo: somehow change api during runtime?
        return new GoogleWebTranslationApi(new JsonMapper());
    }

    @Bean
    public Translate getGoogleCloudTranslate() {
        return TranslateOptions.newBuilder()
                .setProjectId("bht-product-data-translation ")
                .setApiKey(this.appConfig.getApiConfig().getGoogleCloudApiKey()).build().getService();
    }

    @Bean
    public com.deepl.api.Translator getDeeplTranslator() {
        return new com.deepl.api.Translator(this.appConfig.getApiConfig().getDeeplApiKey());
    }
}
