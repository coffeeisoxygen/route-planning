package com.coffeecode.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.model.Locations;
import com.coffeecode.util.distance.GeoToolsCalculator;

@ExtendWith(MockitoExtension.class)
class DistanceServiceTest {

    @Mock
    private GeoToolsCalculator calculator;

    @InjectMocks
    private DistanceService distanceService;

    @Test
    void testCalculateDistanceBandungJakarta() {
        // Arrange
        Locations jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        Locations bandung = new Locations("Bandung", -6.914744, 107.609810);
        double expectedDistance = 118.08; // Updated to match GeoTools calculation

        when(calculator.calculate(-6.200000, 106.816666, -6.914744, 107.609810))
                .thenReturn(expectedDistance);
        when(calculator.getStrategyName()).thenReturn("GeoTools");

        // Act
        double distance = distanceService.calculateDistance(jakarta, bandung);

        // Assert
        assertEquals(expectedDistance, distance, 0.01); // Tighter tolerance
        verify(calculator).calculate(-6.200000, 106.816666, -6.914744, 107.609810);
        verify(calculator).getStrategyName();
    }

    @Test
    void testNullLocations() {
        assertThrows(IllegalArgumentException.class, ()
                -> distanceService.calculateDistance(null, new Locations("Test", 0, 0)));
        assertThrows(IllegalArgumentException.class, ()
                -> distanceService.calculateDistance(new Locations("Test", 0, 0), null));
    }

}
