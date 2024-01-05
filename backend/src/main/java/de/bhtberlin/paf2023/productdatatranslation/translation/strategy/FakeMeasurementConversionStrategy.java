package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

/**
 * Measurement conversion strategy that just returns the original value without
 * converting it. This strategy could be used during testing or development.
 */
public class FakeMeasurementConversionStrategy implements MeasurementConversionStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertMeasurement(double measurement, @NotNull String from, @NotNull String to) {
        return 0;
    }
}
