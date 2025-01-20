package com.coffeecode.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeecode.model.Locations;
import com.coffeecode.repository.LocationRepository;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final DistanceService distanceService;

    @Autowired
    public LocationService(LocationRepository locationRepository, DistanceService distanceService) {
        this.locationRepository = locationRepository;
        this.distanceService = distanceService;
    }

    public Locations addLocation(String name, double latitude, double longitude) {
        Locations location = new Locations(name, latitude, longitude);
        logger.info("Adding new location: {}", location);
        return locationRepository.save(location);
    }

    public Optional<Locations> findById(UUID id) {
        logger.info("Finding location by ID: {}", id);
        return locationRepository.findById(id);
    }

    public Optional<Locations> findByName(String name) {
        logger.info("Finding location by name: {}", name);
        return locationRepository.findByName(name);
    }

    public List<Locations> getAllLocations() {
        logger.info("Retrieving all locations");
        return locationRepository.findAll();
    }

    public void deleteLocation(UUID id) {
        logger.info("Deleting location with ID: {}", id);
        if (!locationRepository.exists(id)) {
            throw new IllegalArgumentException("Location not found with ID: " + id);
        }
        locationRepository.delete(id);
    }

    public boolean locationExists(UUID id) {
        return locationRepository.exists(id);
    }

    public Locations updateLocation(UUID id, String name, double latitude, double longitude) {
        logger.info("Updating location with ID: {}", id);
        if (!locationRepository.exists(id)) {
            throw new IllegalArgumentException("Location not found with ID: " + id);
        }
        Locations updatedLocation = new Locations(id, name, latitude, longitude);
        return locationRepository.save(updatedLocation);
    }

    public double calculateDistance(UUID id1, UUID id2) {
        logger.info("Calculating distance between locations: {} and {}", id1, id2);
        Locations loc1 = locationRepository.findById(id1)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + id1));
        Locations loc2 = locationRepository.findById(id2)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + id2));
        return distanceService.calculateDistance(loc1, loc2);
    }
}
