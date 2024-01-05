package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

/**
 * Strategy for measurement conversion.
 */
public interface MeasurementConversionStrategy {

    /**
     * Convert a measurement.
     *
     * @param measurement The value to convert.
     * @param from        The tag of the current locale.
     * @param to          The tag of the target locale.
     * @return The converted value.
     */
    double convertMeasurement(double measurement, @NotNull String from, @NotNull String to);
}
