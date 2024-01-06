package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MeasurementConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.TextTranslationStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Translator that uses a {@link TextTranslationStrategy}, {@link CurrencyConversionStrategy}
 * and {@link MeasurementConversionStrategy} in order to translate texts, currencies
 * and measurements. This would allow even more flexibility in how different
 * values would be translated.
 * <br>
 * It also implements a builder (with the help of Lombok) to make initialization
 * of the different strategies easier.
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
public class StrategyTranslator extends BaseTranslator {

    private TextTranslationStrategy textStrategy;

    private CurrencyConversionStrategy currencyStrategy;

    private MeasurementConversionStrategy measurementStrategy;

    private AutoTranslationCache translationCache;

    /**
     * Set all strategies.
     *
     * @param textStrategy        The {@link TextTranslationStrategy}.
     * @param currencyStrategy    The {@link CurrencyConversionStrategy}.
     * @param measurementStrategy The {@link MeasurementConversionStrategy}.
     */
    public void setStrategies(TextTranslationStrategy textStrategy,
                              CurrencyConversionStrategy currencyStrategy,
                              MeasurementConversionStrategy measurementStrategy) {
        this.textStrategy = textStrategy;
        this.currencyStrategy = currencyStrategy;
        this.measurementStrategy = measurementStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(String text, String from, String to) {
        return this.translateText(text, from, to, this.translationCache != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(String text, String from, String to, boolean cached) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (cached && this.translationCache != null) {
            return this.translationCache.cachedTranslate(text, from, to, this);
        } else {
            return this.textStrategy.translateText(text, from, to);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, String from, String to) {
        return this.currencyStrategy.convertCurrency(currency, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertMeasurement(double measurement, String from, String to) {
        return this.measurementStrategy.convertMeasurement(measurement, from, to);
    }
}
