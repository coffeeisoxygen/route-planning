package com.coffeecode.util.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DistanceCalculatorFactoryTest {

    @Test
    void getCalculator_Haversine_ReturnsHaversineCalculator() {
        DistanceCalculator calculator = DistanceCalculatorFactory.getDistanceCalculator(DistanceCalulatorName.HAVERSINE);
        assertEquals("Haversine", calculator.getStrategyName());
    }

    @Test
    void getCalculator_Vincenty_ReturnsVincentyCalculator() {
        DistanceCalculator calculator = DistanceCalculatorFactory.getDistanceCalculator(DistanceCalulatorName.VINCENTY);
        assertEquals("Vincenty", calculator.getStrategyName());
    }

    @Test
    void getCalculator_Equirectangular_ReturnsEquirectangularCalculator() {
        DistanceCalculator calculator = DistanceCalculatorFactory.getDistanceCalculator(DistanceCalulatorName.EQUIRECTANGULAR);
        assertEquals("Equirectangular", calculator.getStrategyName());
    }
}