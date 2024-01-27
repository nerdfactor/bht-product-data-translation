package de.bhtberlin.paf2023.productdatatranslation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.translation.caching.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.factory.TranslatorFactory;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyLayerConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MicrosoftTranslationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for translation features.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TranslationConfig {

    /**
     * The application configuration.
     */
    final AppConfig appConfig;

    /**
     * The application context.
     */
    final ApplicationContext context;

    /**
     * Creates a {@link Translator} bean from the configured factory.
     *
     * @return The {@link Translator}.
     * @throws ClassNotFoundException If the configured translator could not be created.
     */
    @Bean
    @Primary
    public Translator getTranslator() throws ClassNotFoundException {
        try {
            String factoryClass = this.createClassName(this.appConfig.getTranslatorConfig().getFactory(), this.appConfig.getTranslatorConfig().getFactoryPackage());
            Class<?> cls = Class.forName(factoryClass);
            TranslatorFactory factory = (TranslatorFactory) cls.getDeclaredConstructor().newInstance();
            Translator translator = factory.getTranslator(this.appConfig.getTranslatorConfig(), context);
            return translator;
        } catch (Exception e) {
            throw new ClassNotFoundException("Could not create translator.", e);
        }
    }

    /**
     * Creates a {@link AutoTranslationCache}.
     *
     * @return The {@link AutoTranslationCache}.
     */
    @Bean
    public AutoTranslationCache getAutoTranslationCache() {
        // todo: make cache configurable?
        return new AutoTranslationCache();
    }

    /**
     * Creates a {@link Translate} for the Google Cloud Translate API.
     *
     * @return The {@link Translate}.
     */
    @Bean
    public Translate getGoogleCloudTranslate() {
        return TranslateOptions.newBuilder()
                .setProjectId(this.appConfig.getTranslatorConfig().getApiConfig().getGoogleCloutApiProject())
                .setApiKey(this.appConfig.getTranslatorConfig().getApiConfig().getGoogleCloudApiKey())
                .build()
                .getService();
    }

    /**
     * Creates a {@link com.deepl.api.Translator} for the DeepL API.
     *
     * @return The {@link com.deepl.api.Translator}.
     */
    @Bean
    public com.deepl.api.Translator getDeeplTranslator() {
        return new com.deepl.api.Translator(this.appConfig.getTranslatorConfig().getApiConfig().getDeeplApiKey());
    }

    /**
     * Creates a {@link MicrosoftTranslationStrategy} for the Microsoft Translator API.
     *
     * @return The {@link MicrosoftTranslationStrategy}.
     */
    @Bean
    public MicrosoftTranslationStrategy getMicrosoftTranslationStrategy() {
        return new MicrosoftTranslationStrategy(this.appConfig.getTranslatorConfig().getApiConfig().getMicrosoftApiKey(),
                this.appConfig.getTranslatorConfig().getApiConfig().getMicrosoftApiRegion(),
                new ObjectMapper());
    }

    /**
     * Creates a {@link CurrencyLayerConversionStrategy} for the CurrencyLayer API.
     *
     * @return The {@link CurrencyLayerConversionStrategy}.
     */
    @Bean
    public CurrencyLayerConversionStrategy getCurrencyConversionApiStrategy() {
        return new CurrencyLayerConversionStrategy(
                this.appConfig.getTranslatorConfig().getApiConfig().getCurrencyLayerApiKey(),
                new ObjectMapper()
        );
    }

    /**
     * Creates a class name from the given class name and package name.
     *
     * @param className   The class name.
     * @param packageName The package name.
     * @return The class name.
     */
    protected @NotNull String createClassName(@NotNull String className, String packageName) {
        if (className.contains(".")) {
            return className;
        }
        return packageName + "." + className;
    }
}
