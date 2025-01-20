package com.coffeecode.util.distance;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DistanceCalculatorTest {

    private final HaversineCalculator haversine = new HaversineCalculator();
    private final VincentyCalculator vincenty = new VincentyCalculator();

    private static Stream<Arguments> cityDistances() {
        return Stream.of(
                // city1, city2, lat1, lon1, lat2, lon2, expectedKm, tolerance
                Arguments.of("Jakarta", "Bandung", -6.200000, 106.816666, -6.914744, 107.609810, 118.29, 0.2),
                Arguments.of("Jakarta", "Surabaya", -6.200000, 106.816666, -7.249720, 112.750830, 692.24, 0.5),
                Arguments.of("Same Point", "Same Point", 0.0, 0.0, 0.0, 0.0, 0.0, 0.001)
        );
    }

    @ParameterizedTest(name = "Distance between {0} and {1}")
    @MethodSource("cityDistances")
    @DisplayName("Haversine Calculator Test")
    void haversineCalculatorTest(
            String city1, String city2,
            double lat1, double lon1,
            double lat2, double lon2,
            double expected, double tolerance) {
        double actual = haversine.calculate(lat1, lon1, lat2, lon2);
        assertEquals(expected, actual, tolerance,
                String.format("Distance between %s and %s should be %.2f ± %.2f km",
                        city1, city2, expected, tolerance));
    }

    @ParameterizedTest(name = "Distance between {0} and {1}")
    @MethodSource("cityDistances")
    @DisplayName("Vincenty Calculator Test")
    void vincentyCalculatorTest(
            String city1, String city2,
            double lat1, double lon1,
            double lat2, double lon2,
            double expected, double tolerance) {
        double actual = vincenty.calculate(lat1, lon1, lat2, lon2);
        assertEquals(expected, actual, tolerance,
                String.format("Distance between %s and %s should be %.2f ± %.2f km",
                        city1, city2, expected, tolerance));
    }
}
