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

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
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
        locationRepository.delete(id);
    }

    public boolean locationExists(UUID id) {
        return locationRepository.exists(id);
    }
}
