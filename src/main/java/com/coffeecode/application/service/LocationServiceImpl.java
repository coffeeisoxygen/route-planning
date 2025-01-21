package com.coffeecode.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeecode.application.port.input.LocationManagementUseCase;
import com.coffeecode.application.port.input.LocationQueryUseCase;
import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.port.output.RouteCalculationPort;
import com.coffeecode.domain.exception.LocationNotFoundException;
import com.coffeecode.domain.model.Locations;

@Service
public class LocationServiceImpl implements
        LocationManagementUseCase,
        LocationQueryUseCase {

    private final LocationPersistancePort persistancePort;
    private final RouteCalculationPort routeCalculationPort;

    @Autowired
    public LocationServiceImpl(
            LocationPersistancePort persistancePort,
            RouteCalculationPort routeCalculationPort) {
        this.persistancePort = persistancePort;
        this.routeCalculationPort = routeCalculationPort;
    }

    @Override
    public Locations addLocation(String name, double lat, double lon) {
        return persistancePort.save(new Locations(name, lat, lon));
    }

    @Override
    public void deleteLocation(UUID id) {
        persistancePort.delete(id);
    }

    @Override
    public void updateLocation(UUID id, String name, double lat, double lon) {
        if (!persistancePort.exists(id)) {
            throw new LocationNotFoundException("Location not found: " + id);
        }
        persistancePort.save(new Locations(id, name, lat, lon));
    }

    @Override
    public List<Locations> getAllLocations() {
        return persistancePort.findAll();
    }

    @Override
    public Optional<Locations> findById(UUID id) {
        return persistancePort.findById(id);
    }

    @Override
    public Optional<Locations> findNearestLocation(double lat, double lon) {
        return persistancePort.findNearestLocation(lat, lon);
    }
}
