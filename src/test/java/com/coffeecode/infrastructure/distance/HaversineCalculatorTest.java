package com.coffeecode.infrastructure.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.coffeecode.domain.model.Locations;

class HaversineCalculatorTest {

    private HaversineCalculator calculator;
    private Locations jakarta;
    private Locations bandung;
    private Locations surabaya;

    @BeforeEach
    void setUp() {
        calculator = new HaversineCalculator();
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);
    }

    @Test
    void calculateDistance_betweenKnownCities_shouldMatchExpectedDistance() {
        double distance = calculator.calculateDistance(jakarta, bandung);

        // Reference distances:
        // - Omni Calculator: 118.000 km
        // - LatLong Data: 118.292 km
        double expectedDistance = 118.15; // Average of reference sources
        double tolerance = 0.5; // 500m tolerance

        assertEquals(expectedDistance, distance, tolerance,
                "Distance should match reference sources within 500m tolerance");
    }

    @ParameterizedTest
    @CsvSource({
        "-91, 0, 0, 0", // Invalid latitude
        "91, 0, 0, 0", // Invalid latitude
        "0, -181, 0, 0", // Invalid longitude
        "0, 181, 0, 0", // Invalid longitude
        "0, 0, -91, 0", // Invalid destination latitude
        "0, 0, 91, 0", // Invalid destination latitude
        "0, 0, 0, -181", // Invalid destination longitude
        "0, 0, 0, 181" // Invalid destination longitude
    })
    void calculateDistance_withInvalidCoordinates_shouldThrowException(
            double fromLat, double fromLon, double toLat, double toLon) {
        assertThrows(DistanceCalculatorException.class, ()
                -> calculator.calculateDistance(fromLat, fromLon, toLat, toLon));
    }

    @Test
    void calculateDistance_samePoint_shouldReturnZero() {
        double distance = calculator.calculateDistance(
                jakarta.latitude(), jakarta.longitude(),
                jakarta.latitude(), jakarta.longitude()
        );
        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void calculateDistance_shouldBeSymmetric() {
        double distance1 = calculator.calculateDistance(jakarta, bandung);
        double distance2 = calculator.calculateDistance(bandung, jakarta);

        assertEquals(distance1, distance2, 0.001);
    }
}
