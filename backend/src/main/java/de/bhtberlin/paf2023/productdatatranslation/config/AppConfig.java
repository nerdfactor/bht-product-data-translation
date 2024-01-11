package de.bhtberlin.paf2023.productdatatranslation.config;

import de.bhtberlin.paf2023.productdatatranslation.translation.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.BasicTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.StrategyTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.factory.BasicTranslatorFactory;
import de.bhtberlin.paf2023.productdatatranslation.translation.factory.StrategyTranslatorFactory;
import de.bhtberlin.paf2023.productdatatranslation.translation.factory.TranslatorFactory;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.FakeCurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.FakeMeasurementConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.GoogleWebTranslationStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.TextTranslationStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application specific configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    // todo: where to put this?
    public static final String DEFAULT_LANGUAGE = "de";

    /**
     * The current application version.
     */
    private String version;

    /**
     * The configuration used for translation.
     */
    private TranslatorConfig translatorConfig = new TranslatorConfig();

    @Getter
    @Setter
    public static class TranslatorConfig {

        private String factoryPackage = TranslatorFactory.class.getPackageName();

        private String translatorPackage = Translator.class.getPackageName();

        private String strategyPackage = TextTranslationStrategy.class.getPackageName();

        private String factory = BasicTranslatorFactory.class.getCanonicalName();

        private String translator = BasicTranslator.class.getSimpleName();

        /**
         * The configuration for the translation strategies.
         * This will only be used if the translator is a {@link StrategyTranslator}
         * created by a {@link StrategyTranslatorFactory}.
         */
        private StrategyConfig strategyConfig = new StrategyConfig();

        /**
         * The configuration for external Apis.
         */
        private ExternalApiConfig apiConfig = new ExternalApiConfig();


        @Getter
        @Setter
        public static class StrategyConfig {
            private String textTranslationStrategy = GoogleWebTranslationStrategy.class.getSimpleName();

            private String currencyConversionStrategy = FakeCurrencyConversionStrategy.class.getSimpleName();

            private String measurementConversionStrategy = FakeMeasurementConversionStrategy.class.getSimpleName();

            private String translationCache = AutoTranslationCache.class.getSimpleName();
        }

        @Getter
        @Setter
        public static class ExternalApiConfig {
            // todo: combine to one field apiKey instead of object with multiple keys?
            private String deeplApiKey;
            private String googleCloudApiKey;
            private String googleCloutApiProject;
            private String microsoftApiKey;
            private String microsoftApiRegion;
        }
    }
}
