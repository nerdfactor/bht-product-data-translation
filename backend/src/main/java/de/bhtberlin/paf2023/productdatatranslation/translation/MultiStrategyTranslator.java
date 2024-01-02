package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MeasurementConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.TextTranslationStrategy;
import lombok.*;
import org.jetbrains.annotations.NotNull;

/**
 * Translator that uses a {@link TextTranslationStrategy}, {@link CurrencyConversionStrategy}
 * and {@link MeasurementConversionStrategy} in order to translate texts, currencies
 * and measurements.
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
public class MultiStrategyTranslator extends BaseTranslator {

    private TextTranslationStrategy textStrategy;

    private CurrencyConversionStrategy currencyStrategy;

    private MeasurementConversionStrategy measurementStrategy;

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
        if (text == null || text.isEmpty()) {
            return "";
        }
        return this.textStrategy.translateText(text, from, to);
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
