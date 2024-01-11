package de.bhtberlin.paf2023.productdatatranslation.translation.factory;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.translation.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.StrategyTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MeasurementConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.TextTranslationStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ListableBeanFactory;

import java.lang.reflect.Method;

public class StrategyTranslatorFactory extends BasicTranslatorFactory {

    @Override
    public Translator getTranslator(AppConfig.@NotNull TranslatorConfig config, ListableBeanFactory beanFactory) {
        String translatorClassName = createClassName(config.getTranslator(), config.getTranslatorPackage());
        String textTranslationStrategyClassName = createClassName(config.getStrategyConfig().getTextTranslationStrategy(), config.getStrategyPackage());
        String currencyConversionStrategyClassName = createClassName(config.getStrategyConfig().getCurrencyConversionStrategy(), config.getStrategyPackage());
        String measurementConversionStrategyClassName = createClassName(config.getStrategyConfig().getMeasurementConversionStrategy(), config.getStrategyPackage());
        String translationCacheClassName = createClassName(config.getStrategyConfig().getTranslationCache(), config.getTranslatorPackage());

        try {
            Class<?> translatorClass = Class.forName(translatorClassName);
            Method builderMethod = translatorClass.getMethod("builder");
            StrategyTranslator.StrategyTranslatorBuilder builder = (StrategyTranslator.StrategyTranslatorBuilder) builderMethod.invoke(null);

            return builder.withTextStrategy((TextTranslationStrategy) createClass(Class.forName(textTranslationStrategyClassName), beanFactory))
                    .withCurrencyStrategy((CurrencyConversionStrategy) createClass(Class.forName(currencyConversionStrategyClassName), beanFactory))
                    .withMeasurementStrategy((MeasurementConversionStrategy) createClass(Class.forName(measurementConversionStrategyClassName), beanFactory))
                    .withTranslationCache((AutoTranslationCache) createClass(Class.forName(translationCacheClassName), beanFactory))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Could not create translator.", e);
        }
    }
}
