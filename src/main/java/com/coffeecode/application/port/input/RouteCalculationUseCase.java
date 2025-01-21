package com.coffeecode.application.port.input;

import java.util.List;
import java.util.UUID;

import com.coffeecode.domain.model.Locations;

public interface RouteCalculationUseCase {

    List<Locations> calculateRoute(UUID startId, UUID endId);

    double calculateDistance(UUID loc1Id, UUID loc2Id);
}
