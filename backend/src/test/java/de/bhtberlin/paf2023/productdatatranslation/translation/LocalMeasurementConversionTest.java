package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.LocalMeasurementConversionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalMeasurementConversionTest {

    LocalMeasurementConversionStrategy api = new LocalMeasurementConversionStrategy();

    @Test
    void shouldConvertMeasurements() {
        double expectedCmToIn = 0.393701;
        double expectedInToCm = 2.54;
        double expectedKgToLb = 2.20462;
        double expectedLbToKg = 0.453592;
        double cmToIn = api.convertMeasurement(1, "cm", "in");
        double inToCm = api.convertMeasurement(1, "in", "cm");
        double kgToLb = api.convertMeasurement(1, "kg", "lb");
        double lbToKg = api.convertMeasurement(1, "lb", "kg");
        Assertions.assertEquals(expectedCmToIn, cmToIn);
        Assertions.assertEquals(expectedInToCm, inToCm);
        Assertions.assertEquals(expectedKgToLb, kgToLb);
        Assertions.assertEquals(expectedLbToKg, lbToKg);
    }
}
