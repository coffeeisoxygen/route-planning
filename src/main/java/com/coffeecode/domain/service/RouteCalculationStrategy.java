package com.coffeecode.domain.service;

import java.util.List;
import com.coffeecode.domain.model.Locations;

public interface RouteCalculationStrategy {

    List<Locations> calculateShortestPath(Locations start, Locations end);

    double calculateDistance(Locations from, Locations to);
}
