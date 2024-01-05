package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

/**
 * Strategy for currency conversion.
 */
public interface CurrencyConversionStrategy {

    /**
     * Convert a currency.
     *
     * @param currency The value to convert.
     * @param from     The tag of the current locale.
     * @param to       The tag of the target locale.
     * @return The converted value.
     */
    double convertCurrency(double currency, @NotNull String from, @NotNull String to);
}
