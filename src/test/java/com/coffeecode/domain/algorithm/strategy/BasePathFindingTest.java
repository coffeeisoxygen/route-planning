package com.coffeecode.domain.algorithm.strategy;

import org.junit.jupiter.api.BeforeEach;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

public abstract class BasePathFindingTest {

    protected RouteMap routeMap;
    protected Locations bandung, jakarta, surabaya;

    @BeforeEach
    void setUp() {
        GeoToolsCalculator distanceCalculator = new GeoToolsCalculator();
        routeMap = new RouteMap(distanceCalculator);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);
        routeMap.addLocation(surabaya);
    }
}
