package com.coffeecode.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.model.Locations;
import com.coffeecode.util.distance.DistanceCalculator;
import com.coffeecode.util.distance.DistanceCalculatorFactory;

@ExtendWith(MockitoExtension.class)
class DistanceServiceTest {

    @Mock
    private DistanceCalculator mockCalculator;
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockCalculator.getStrategyName()).thenReturn("MOCK");

        try (MockedStatic<DistanceCalculatorFactory> factory = mockStatic(DistanceCalculatorFactory.class)) {
            factory.when(() -> DistanceCalculatorFactory.getDistanceCalculator(any()))
                    .thenReturn(mockCalculator);
            distanceService = new DistanceService();
        }
    }

    @Test
    void testCalculateDistance() {
        // Arrange
        Locations loc1 = new Locations("Location1", 10.0, 20.0);
        Locations loc2 = new Locations("Location2", 30.0, 40.0);
        when(mockCalculator.calculate(10.0, 20.0, 30.0, 40.0)).thenReturn(100.0);

        // Act
        double distance = distanceService.calculateDistance(loc1, loc2);

        // Assert
        assertEquals(100.0, distance);
        verify(mockCalculator).calculate(10.0, 20.0, 30.0, 40.0);
    }

}
