package com.coffeecode.util.distance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opengis.referencing.FactoryException;

@ExtendWith(MockitoExtension.class)
class GeoToolsCalculatorTest {

    private GeoToolsCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new GeoToolsCalculator();
    }

    @BeforeAll
    static void initClass() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @Test
    void testCalculateDistance() {
        // New York (lat, lon)
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        // London (lat, lon)
        double lat2 = 51.5074;
        double lon2 = -0.1278;

        double distance = calculator.calculate(lat1, lon1, lat2, lon2);
        // Expected distance approximately 5570 km
        assertEquals(5570, distance, 100); // Allow 100 km tolerance
    }

    @Test
    void testZeroDistance() {
        double lat = 40.7128;
        double lon = -74.0060;

        double distance = calculator.calculate(lat, lon, lat, lon);
        assertEquals(0.0, distance, 0.01);
    }

    @Test
    void testAntipodalPoints() {
        // New York
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        // Antipode
        double lat2 = -40.7128;
        double lon2 = 105.9940;

        double distance = calculator.calculate(lat1, lon1, lat2, lon2);
        // Should be approximately 20,000 km
        assertEquals(20000, distance, 100); // Allow 100 km tolerance
    }

    @Test
    void testCRSInitializationFailure() {
        try (MockedStatic<CRS> mockedCRS = mockStatic(CRS.class)) {
            mockedCRS.when(() -> CRS.decode("EPSG:4326", true))
                    .thenThrow(new FactoryException("CRS initialization failed"));

            GeoToolsException exception = assertThrows(GeoToolsException.class,
                    () -> new GeoToolsCalculator());
            assertTrue(exception.getMessage().contains("Failed to initialize CRS"));
        }
    }

    @Test
    void testGetStrategyName() {
        assertEquals("GeoTools", calculator.getStrategyName());
    }

    @Test
    void testJakartaToBandungDistance() {
        // Jakarta coordinates
        double lat1 = -6.200000;
        double lon1 = 106.816666;

        // Bandung coordinates
        double lat2 = -6.914744;
        double lon2 = 107.609810;

        double distance = calculator.calculate(lat1, lon1, lat2, lon2);

        // Expected distance should be approximately 118.3 km
        assertEquals(118.3, distance, 1.0); // Allow 1 km tolerance
    }
}
