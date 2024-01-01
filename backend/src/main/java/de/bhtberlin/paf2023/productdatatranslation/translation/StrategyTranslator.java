package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.MultiTranslationStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Translator that uses a {@link MultiTranslationStrategy} in order to translate
 * texts, currencies and measurements.
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
public class StrategyTranslator extends BaseTranslator {

    private MultiTranslationStrategy strategy;

    /**
     * {@inheritDoc}
     */
    @Override
    public String translateText(String string, String from, String to) {
        return this.strategy.translateText(string, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, String from, String to) {
        return this.strategy.convertCurrency(currency, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertMeasurement(double measurement, String from, String to) {
        return this.strategy.convertMeasurement(measurement, from, to);
    }
}
