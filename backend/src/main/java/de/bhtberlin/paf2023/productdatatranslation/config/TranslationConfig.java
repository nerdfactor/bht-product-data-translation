package de.bhtberlin.paf2023.productdatatranslation.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.translation.StrategyTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.GoogleWebTranslationStrategy;
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
    public Translator getTranslator() {
        // todo: get from configuration.
        // todo: somehow change during runtime?
        return StrategyTranslator.builder()
                .withTextStrategy(new GoogleWebTranslationStrategy(new JsonMapper()))
                .withCurrencyStrategy(new FakeCurrencyConversionStrategy())
                .withMeasurementStrategy(new FakeMeasurementConversionStrategy())
                .build();
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
