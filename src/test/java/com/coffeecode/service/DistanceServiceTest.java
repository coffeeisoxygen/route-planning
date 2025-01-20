package com.coffeecode.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.coffeecode.model.Locations;
import com.coffeecode.util.distance.DistanceCalculator;
import com.coffeecode.util.distance.DistanceCalculatorFactory;
import com.coffeecode.util.distance.DistanceCalulatorName;

public class DistanceServiceTest {

    private DistanceService distanceService;
    private DistanceCalculator mockCalculator;

    @BeforeEach
    public void setUp() {
        mockCalculator = mock(DistanceCalculator.class);
        try (MockedStatic<DistanceCalculatorFactory> factoryMock = mockStatic(DistanceCalculatorFactory.class)) {
            factoryMock.when(() -> DistanceCalculatorFactory.getDistanceCalculator(any(DistanceCalulatorName.class)))
                    .thenReturn(mockCalculator);
            distanceService = new DistanceService();
        }
    }

    @Test
    public void testCalculateDistance() {
        Locations loc1 = new Locations("Location1", 10.0, 20.0);
        Locations loc2 = new Locations("Location2", 30.0, 40.0);

        when(mockCalculator.calculate(10.0, 20.0, 30.0, 40.0)).thenReturn(100.0);

        double distance = distanceService.calculateDistance(loc1, loc2);

        assertEquals(100.0, distance);
        verify(mockCalculator).calculate(10.0, 20.0, 30.0, 40.0);
    }

    @Test
    public void testCalculateDistanceWithNullLocations() {
        assertThrows(IllegalArgumentException.class, () -> {
            distanceService.calculateDistance(null, new Locations("Location2", 30.0, 40.0));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            distanceService.calculateDistance(new Locations("Location1", 10.0, 20.0), null);
        });
    }

    @Test
    public void testSetCalculationStrategy() {
        try (MockedStatic<DistanceCalculatorFactory> factoryMock = mockStatic(DistanceCalculatorFactory.class)) {
            factoryMock.when(() -> DistanceCalculatorFactory.getDistanceCalculator(DistanceCalulatorName.VINCENTY))
                    .thenReturn(mockCalculator);

            distanceService.setCalculationStrategy(DistanceCalulatorName.VINCENTY);

            verify(mockCalculator, never()).calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        }
    }
}
