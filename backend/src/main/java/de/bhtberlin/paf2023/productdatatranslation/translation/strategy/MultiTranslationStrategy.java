package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

/**
 * Strategy that combines text, currency and measurement strategies.
 */
public interface MultiTranslationStrategy extends TextTranslationStrategy, CurrencyConversionStrategy, MeasurementConversionStrategy {

}
