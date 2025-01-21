package com.coffeecode.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.coffeecode.application.port.input.RouteCalculationUseCase;
import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.port.output.RouteCalculationPort;
import com.coffeecode.domain.model.Locations;

@Service
public class RouteServiceImpl implements RouteCalculationUseCase {

    private final LocationPersistancePort locationPort;
    private final RouteCalculationPort routePort;

    public RouteServiceImpl(LocationPersistancePort locationPort,
            RouteCalculationPort routePort) {
        this.locationPort = locationPort;
        this.routePort = routePort;
    }

    @Override
    public List<Locations> calculateRoute(UUID startId, UUID endId) {
        Locations start = locationPort.findById(startId)
                .orElseThrow(() -> new IllegalArgumentException("Start location not found"));
        Locations end = locationPort.findById(endId)
                .orElseThrow(() -> new IllegalArgumentException("End location not found"));

        return routePort.findShortestPath(start, end);
    }

    @Override
    public double calculateDistance(UUID loc1Id, UUID loc2Id) {
        Locations loc1 = locationPort.findById(loc1Id)
                .orElseThrow(() -> new IllegalArgumentException("Location 1 not found"));
        Locations loc2 = locationPort.findById(loc2Id)
                .orElseThrow(() -> new IllegalArgumentException("Location 2 not found"));

        return routePort.calculateDistance(loc1, loc2);
    }
}
