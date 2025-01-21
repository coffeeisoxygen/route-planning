package com.coffeecode.gui.controllers;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.exception.LocationOperationException;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.service.LocationService;

@Component
public class LocationController {

    private final LocationService service;
    private final LocationTableModel tableModel;

    @Autowired
    public LocationController(LocationService locationService, LocationTableModel tableModel) {
        this.service = locationService;
        this.tableModel = tableModel;
        initialize(); // Load initial data
    }

    @PostConstruct
    private void initialize() {
        refreshLocations();
    }

    public void refreshLocations() {
        tableModel.updateLocations(service.getAllLocations());
    }

    public void addLocation(String name, double lat, double lon) {
        try {
            service.addLocation(name, lat, lon);
            updateTableModel(); // Ensure this is called
        } catch (IllegalArgumentException e) {
            throw new LocationOperationException("Failed to add location", e);
        }
    }

    public void deleteLocation(UUID id) {
        try {
            service.deleteLocation(id);
            refreshLocations();
        } catch (Exception e) {
            throw new LocationOperationException("Failed to delete location", e);
        }
    }

    public void updateLocation(UUID id, String name, double lat, double lon) {
        try {
            service.updateLocation(id, name, lat, lon);
            refreshLocations();
        } catch (Exception e) {
            throw new LocationOperationException("Failed to update location", e);
        }
    }

    private void updateTableModel() {
        tableModel.setLocations(service.getAllLocations());
    }

}
