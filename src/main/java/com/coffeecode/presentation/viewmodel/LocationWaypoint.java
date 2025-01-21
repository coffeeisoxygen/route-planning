package com.coffeecode.presentation.viewmodel;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import com.coffeecode.domain.model.Locations;

public class LocationWaypoint extends DefaultWaypoint {

    private final Locations location;

    public LocationWaypoint(Locations location) {
        super(new GeoPosition(location.latitude(), location.longitude()));
        this.location = location;
    }

    public Locations getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return location.name();
    }
}
