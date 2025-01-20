package com.coffeecode.util.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HaversineCalculatorTest {
    private final HaversineCalculator calculator = new HaversineCalculator();

    @ParameterizedTest
    @CsvSource({
        "-6.200000, 106.816666, -6.914744, 107.609810, 118.29", // Jakarta-Bandung
        "-6.200000, 106.816666, -7.249720, 112.750830, 692.24"  // Jakarta-Surabaya
    })
    void calculate_KnownLocations_ReturnsExpectedDistance(
            double lat1, double lon1, 
            double lat2, double lon2, 
            double expected) {
        assertEquals(expected, calculator.calculate(lat1, lon1, lat2, lon2), 0.1);
    }
}