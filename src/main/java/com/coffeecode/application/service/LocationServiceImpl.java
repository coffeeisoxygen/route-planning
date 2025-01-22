package com.coffeecode.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.coffeecode.application.port.input.LocationManagementUseCase;
import com.coffeecode.application.port.input.LocationQueryUseCase;
import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.domain.exception.LocationNotFoundException;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.RouteMap;

@Service
@Primary
public class LocationServiceImpl implements
        LocationManagementUseCase,
        LocationQueryUseCase {

    private final LocationPersistancePort persistancePort;
    private final RouteMap routeMap;

    @Autowired
    public LocationServiceImpl(
            LocationPersistancePort persistancePort,
            RouteMap routeMap) {
        this.persistancePort = persistancePort;
        this.routeMap = routeMap;
    }

    @Override
    public Locations addLocation(String name, double lat, double lon) {
        Locations newLocation = new Locations(name, lat, lon);
        Locations saved = persistancePort.save(newLocation);
        routeMap.addLocation(saved); // This will create routes automatically
        return saved;
    }

    @Override
    public void deleteLocation(UUID id) {
        persistancePort.findById(id).ifPresent(location -> {
            persistancePort.delete(id);
            // RouteMap will handle route cleanup
        });
    }

    @Override
    public void updateLocation(UUID id, String name, double lat, double lon) {
        if (!persistancePort.exists(id)) {
            throw new LocationNotFoundException("Location not found: " + id);
        }
        Locations updated = new Locations(id, name, lat, lon);
        persistancePort.save(updated);
        routeMap.addLocation(updated); // This will update routes automatically
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
