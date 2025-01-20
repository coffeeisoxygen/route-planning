package com.coffeecode.util.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class EquirectangularCalculatorTest {
    private final EquirectangularCalculator calculator = new EquirectangularCalculator();

    @Test
    void calculate_SamePoint_ReturnsZero() {
        double distance = calculator.calculate(0, 0, 0, 0);
        assertEquals(0, distance, 0.001);
    }

    @Test
    void calculate_KnownDistance_ReturnsExpectedValue() {
        double distance = calculator.calculate(-6.200000, 106.816666, -6.914744, 107.609810);
        assertTrue(distance > 0);
    }
}