package com.coffeecode.gui.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.event.LocationUpdateListener;
import com.coffeecode.model.Locations;
import com.coffeecode.service.LocationService;

@Component
public class MapViewController {

    private final LocationService locationService;
    private final List<LocationUpdateListener> listeners = new ArrayList<>();

    @Autowired
    public MapViewController(LocationService locationService) {
        this.locationService = locationService;
    }

    public void addListener(LocationUpdateListener listener) {
        listeners.add(listener);
    }

    public void selectLocation(Locations location) {
        listeners.forEach(l -> l.onLocationSelected(location));
    }

    public void calculateRoute(Locations start, Locations end) {
        double distance = locationService.calculateDistance(start.id(), end.id());
        listeners.forEach(l -> l.onRouteCalculated(start, end, distance));
    }

    public void updateLocations() {
        List<Locations> locations = locationService.getAllLocations();
        listeners.forEach(l -> l.onLocationsUpdated(locations));
    }
}
