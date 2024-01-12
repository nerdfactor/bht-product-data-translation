package de.bhtberlin.paf2023.productdatatranslation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.translation.AutoTranslationCache;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TranslationConfig {

    final AppConfig appConfig;

    final ApplicationContext context;

    @Bean
    @Primary
    public Translator getTranslator() throws ClassNotFoundException {
        try {
            String factoryClass = this.createClassName(this.appConfig.getTranslatorConfig().getFactory(), this.appConfig.getTranslatorConfig().getFactoryPackage());
            Class<?> cls = Class.forName(factoryClass);
            TranslatorFactory factory = (TranslatorFactory) cls.getDeclaredConstructor().newInstance();
            return factory.getTranslator(this.appConfig.getTranslatorConfig(), context);
        } catch (Exception e) {
            throw new ClassNotFoundException("Could not create translator.", e);
        }
    }

    @Bean
    public AutoTranslationCache getAutoTranslationCache() {
        // todo: make cache configurable?
        return new AutoTranslationCache();
    }

    @Bean
    public Translate getGoogleCloudTranslate() {
        return TranslateOptions.newBuilder()
                .setProjectId(this.appConfig.getTranslatorConfig().getApiConfig().getGoogleCloutApiProject())
                .setApiKey(this.appConfig.getTranslatorConfig().getApiConfig().getGoogleCloudApiKey())
                .build()
                .getService();
    }

    @Bean
    public com.deepl.api.Translator getDeeplTranslator() {
        return new com.deepl.api.Translator(this.appConfig.getTranslatorConfig().getApiConfig().getDeeplApiKey());
    }

    @Bean
    public MicrosoftTranslationStrategy getMicrosoftTranslationStrategy() {
        return new MicrosoftTranslationStrategy(this.appConfig.getTranslatorConfig().getApiConfig().getMicrosoftApiKey(),
                this.appConfig.getTranslatorConfig().getApiConfig().getMicrosoftApiRegion(),
                new ObjectMapper());
    }

    @Bean
    public CurrencyLayerConversionStrategy getCurrencyConversionApiStrategy() {
        return new CurrencyLayerConversionStrategy(
                this.appConfig.getTranslatorConfig().getApiConfig().getCurrencyLayerApiKey(),
                new ObjectMapper()
        );
    }

    protected @NotNull String createClassName(@NotNull String className, String packageName) {
        if (className.contains(".")) {
            return className;
        }
        return packageName + "." + className;
    }
}
