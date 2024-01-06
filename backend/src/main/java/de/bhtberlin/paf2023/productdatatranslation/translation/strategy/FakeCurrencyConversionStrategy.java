package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

/**
 * Currency conversion strategy that just returns the original value without
 * converting it. This strategy could be used during testing or development.
 */
public class FakeCurrencyConversionStrategy implements CurrencyConversionStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, @NotNull String from, @NotNull String to) {
        return currency;
    }
}
