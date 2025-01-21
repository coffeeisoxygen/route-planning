package com.coffeecode.gui.controllers;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.service.LocationService;

@Component
public class LocationController {
    private final LocationService locationService;
    private final LocationTableModel tableModel;

    @Autowired
    public LocationController(LocationService locationService, LocationTableModel tableModel) {
        this.locationService = locationService;
        this.tableModel = tableModel;
        initialize(); // Load initial data
    }

    @PostConstruct
    private void initialize() {
        refreshLocations();
    }

    public void refreshLocations() {
        tableModel.updateLocations(locationService.getAllLocations());
    }

    public void addLocation(String name, double lat, double lon) {
        try {
            locationService.addLocation(name, lat, lon);
            refreshLocations();
        } catch (IllegalArgumentException e) {
            throw new LocationOperationException("Failed to add location", e);
        }
    }

    public void deleteLocation(UUID id) {
        try {
            locationService.deleteLocation(id);
            refreshLocations();
        } catch (Exception e) {
            throw new LocationOperationException("Failed to delete location", e);
        }
    }

    public void updateLocation(UUID id, String name, double lat, double lon) {
        try {
            locationService.updateLocation(id, name, lat, lon);
            refreshLocations();
        } catch (Exception e) {
            throw new LocationOperationException("Failed to update location", e);
        }
    }

}
