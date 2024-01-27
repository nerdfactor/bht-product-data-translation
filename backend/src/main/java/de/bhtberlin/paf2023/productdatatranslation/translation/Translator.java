package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Translates texts, currencies and measurements.
 */
public interface Translator extends TranslationVisitor {

    /**
     * Translate a text.
     *
     * @param text The text to translate.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The translated text.
     */
    @NotNull String translateText(@Nullable String text, String from, String to);

    /**
     * Convert a currency.
     *
     * @param currency The value to convert.
     * @param from     The tag of the current currency.
     * @param to       The tag of the target currency.
     * @return The converted value.
     */
    double convertCurrency(double currency, String from, String to);

    /**
     * Convert a measurement.
     *
     * @param measurement The value to convert.
     * @param from        The tag of the current measurement.
     * @param to          The tag of the target measurement.
     * @return The converted value.
     */
    double convertMeasurement(double measurement, String from, String to);
}
