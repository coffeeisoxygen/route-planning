package com.coffeecode.application.port.output;

import java.util.List;

import com.coffeecode.domain.model.Locations;

public interface RouteCalculationPort {

    double calculateDistance(Locations from, Locations to);

    List<Locations> findShortestPath(Locations start, Locations end);
}
