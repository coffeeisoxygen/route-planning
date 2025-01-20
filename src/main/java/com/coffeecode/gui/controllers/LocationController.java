package com.coffeecode.gui.controllers;

import com.coffeecode.service.LocationService;
import com.coffeecode.model.Locations;
import com.coffeecode.gui.models.LocationTableModel;

public class LocationController {
    private final LocationService locationService;
    private final LocationTableModel tableModel;

    public LocationController(LocationService locationService, LocationTableModel tableModel) {
        this.locationService = locationService;
        this.tableModel = tableModel;
    }

    public void refreshLocations() {
        tableModel.updateLocations(locationService.getAllLocations());
    }

    public void addLocation(String name, double lat, double lon) {
        locationService.addLocation(name, lat, lon);
        refreshLocations();
    }
}
