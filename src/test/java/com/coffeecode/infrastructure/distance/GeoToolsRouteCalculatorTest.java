package com.coffeecode.infrastructure.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;

class GeoToolsRouteCalculatorTest {

    private GeoToolsRouteCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new GeoToolsRouteCalculator();
    }

    @Test
    void shouldCalculateDistanceBetweenTwoPoints() {
        Locations jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        Locations bandung = new Locations("Bandung", -6.914744, 107.609810);

        double distance = calculator.calculateDistance(jakarta, bandung);

        // GeoTools should give more accurate distance
        assertTrue(distance > 118 && distance < 130);
    }

    @Test
    void shouldFindShortestPath() {
        Locations start = new Locations("Start", 0, 0);
        Locations end = new Locations("End", 1, 1);

        var path = calculator.findShortestPath(start, end);

        assertNotNull(path);
        assertEquals(2, path.size());
        assertEquals(start, path.get(0));
        assertEquals(end, path.get(1));
    }
}
