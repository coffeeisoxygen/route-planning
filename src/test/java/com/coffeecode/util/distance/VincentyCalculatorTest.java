package com.coffeecode.util.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class VincentyCalculatorTest {

    private final VincentyCalculator calculator = new VincentyCalculator();

    @Test
    void calculate_SamePoint_ReturnsZero() {
        double distance = calculator.calculate(0, 0, 0, 0);
        assertEquals(0, distance, 0.001);
    }

    @Test
    void calculate_KnownDistance_ReturnsExpectedValue() {
        double distance = calculator.calculate(-6.200000, 106.816666, -6.914744, 107.609810);
        assertEquals(118.29, distance, 0.1);
    }
}

