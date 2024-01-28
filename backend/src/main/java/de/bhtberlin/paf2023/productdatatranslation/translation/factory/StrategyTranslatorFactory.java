package de.bhtberlin.paf2023.productdatatranslation.translation.factory;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.translation.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.StrategyTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MeasurementConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.TextTranslationStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

/**
 * Factory for creating {@link StrategyTranslator StrategyTranslators} and all
 * inheriting classes. This will use the configured strategies in addition to
 * what {@link BasicTranslatorFactory} does.
 * Change the Creation of the {@link Translator} by adding a {@link Bean} for it.
 */
public class StrategyTranslatorFactory extends BasicTranslatorFactory {

    @Override
    public Translator getTranslator(AppConfig.@NotNull TranslatorConfig config, ListableBeanFactory beanFactory) throws ClassNotFoundException {
        String translatorClassName = createClassName(config.getTranslator(), config.getTranslatorPackage());
        String textTranslationStrategyClassName = createClassName(config.getStrategyConfig().getTextTranslationStrategy(), config.getStrategyPackage());
        String currencyConversionStrategyClassName = createClassName(config.getStrategyConfig().getCurrencyConversionStrategy(), config.getStrategyPackage());
        String measurementConversionStrategyClassName = createClassName(config.getStrategyConfig().getMeasurementConversionStrategy(), config.getStrategyPackage());
        String translationCacheClassName = createClassName(config.getStrategyConfig().getTranslationCache(), config.getTranslatorPackage());

        try {
            // Check if the translator is a valid StrategyTranslator
            Class<?> translatorClass = Class.forName(translatorClassName);
            if (!StrategyTranslator.class.isAssignableFrom(translatorClass)) {
                throw new ClassNotFoundException(translatorClassName + " is not a valid StrategyTranslator.");
            }

            // Create the translator and add all strategies.
            StrategyTranslator translator = (StrategyTranslator) this.createClass(translatorClass, beanFactory);
            translator.setTextStrategy((TextTranslationStrategy) createClass(Class.forName(textTranslationStrategyClassName), beanFactory));
            translator.setCurrencyStrategy((CurrencyConversionStrategy) createClass(Class.forName(currencyConversionStrategyClassName), beanFactory));
            translator.setMeasurementStrategy((MeasurementConversionStrategy) createClass(Class.forName(measurementConversionStrategyClassName), beanFactory));
            translator.setTranslationCache((AutoTranslationCache) createClass(Class.forName(translationCacheClassName), beanFactory));
            return translator;
        } catch (Exception e) {
            throw new ClassNotFoundException("Could not create translator.", e);
        }
    }
}
