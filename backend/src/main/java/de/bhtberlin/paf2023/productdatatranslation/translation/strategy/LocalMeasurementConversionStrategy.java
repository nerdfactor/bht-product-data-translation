package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

public class LocalMeasurementConversionStrategy implements MeasurementConversionStrategy {

    private static final Map<String, Double> CONVERSIONS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("cmin", 0.393701),
            new AbstractMap.SimpleEntry<>("incm", 2.54),
            new AbstractMap.SimpleEntry<>("kglb", 2.20462),
            new AbstractMap.SimpleEntry<>("lbkg", 0.453592)
    );

    @Override
    public double convertMeasurement(double measurement, @NotNull String from, @NotNull String to) {
        if (from.equalsIgnoreCase(to)) {
            return measurement;
        }
        String conversionKey = (from + to).toLowerCase();
        if (CONVERSIONS.containsKey(conversionKey)) {
            return measurement * CONVERSIONS.get(conversionKey);
        }
        return measurement;
    }
}
