package com.coffeecode.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.application.services.LocationService;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.gui.event.LocationUpdateListener;
import com.coffeecode.gui.models.MapViewModel;

import javax.swing.JOptionPane;

@Component
public class MapViewController {

    private static final Logger logger = LoggerFactory.getLogger(MapViewController.class);

    private final LocationService locationService;
    private final MapViewModel model;
    private final List<LocationUpdateListener> listeners = new ArrayList<>();
    private Locations selectedLocation;

    @Autowired
    public MapViewController(LocationService locationService, MapViewModel model) {
        this.locationService = locationService;
        this.model = model;
    }

    // Location Management
    public void addLocation(String name, double lat, double lon) {
        try {
            Locations location = locationService.addLocation(name, lat, lon);
            model.setSelectedLocation(location);
            logger.debug("Added location: {}", location);
            updateLocations();
        } catch (Exception e) {
            logger.error("Failed to add location", e);
        }
    }

    public void deleteLocation(UUID id) {
        try {
            locationService.deleteLocation(id);
            logger.debug("Deleted location: {}", id);
            updateLocations();
        } catch (Exception e) {
            logger.error("Failed to delete location", e);
        }
    }

    public void updateLocation(UUID id, String name, double lat, double lon) {
        try {
            locationService.updateLocation(id, name, lat, lon);
            logger.debug("Updated location: {}", id);
            updateLocations();
        } catch (Exception e) {
            logger.error("Failed to update location", e);
        }
    }

    public void addNewLocation(String name, double lat, double lon) {
        try {
            Locations newLocation = new Locations(name, lat, lon);
            locationService.addLocation(newLocation.name(), newLocation.latitude(), newLocation.longitude());

            // Notify listeners
            List<Locations> updated = locationService.getAllLocations();
            listeners.forEach(l -> l.onLocationsUpdated(updated));

            logger.info("Added new location: {}", newLocation);
        } catch (Exception e) {
            logger.error("Failed to add location", e);
            JOptionPane.showMessageDialog(null,
                    "Failed to add location: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Route Operations
    public void calculateRoute(Locations start, Locations end) {
        try {
            double distance = locationService.calculateDistance(start.id(), end.id());
            List<Locations> path = List.of(start, end); // Placeholder for actual path
            logger.debug("Calculated route: {} to {}, distance: {}", start.name(), end.name(), distance);
            listeners.forEach(l -> l.onPathCalculated(start, end, path));
        } catch (Exception e) {
            logger.error("Failed to calculate route", e);
        }
    }

    // Selection Management
    public void selectLocation(Locations location) {
        selectedLocation = location;
        logger.debug("Selected location: {}", location);
        listeners.forEach(l -> l.onLocationSelected(location));
    }

    public void selectLocation(double lat, double lon) {
        locationService.findNearestLocation(lat, lon)
                .ifPresent(location -> {
                    listeners.forEach(l -> l.onLocationSelected(location));
                    logger.debug("Selected location: {}", location);
                });
    }

    public Locations getSelectedLocation() {
        return selectedLocation;
    }

    // Map View Controls
    public void centerOnLocation(Locations location) {
        if (location != null) {
            selectLocation(location);
        }
    }

    public void zoomToFitAll() {
        List<Locations> locations = locationService.getAllLocations();
        if (!locations.isEmpty()) {
            listeners.forEach(l -> l.onLocationsUpdated(locations));
        }
    }

    // Listener Management
    public void addListener(LocationUpdateListener listener) {
        listeners.add(listener);
        updateLocations(); // Initial update
    }

    public void removeListener(LocationUpdateListener listener) {
        listeners.remove(listener);
    }

    // Update Notifications
    public void updateLocations() {
        List<Locations> locations = locationService.getAllLocations();
        model.setWaypoints(locations);
        logger.debug("Updating {} locations", locations.size());
        listeners.forEach(l -> l.onLocationsUpdated(locations));
    }
}
