package com.coffeecode.gui.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.springframework.stereotype.Component;

import com.coffeecode.domain.model.Locations;

@Component
public class MapViewModel {

    private Set<Waypoint> waypoints = new HashSet<>();
    private List<GeoPosition> routePath;
    private GeoPosition center;
    private int zoom = 8;
    private Locations selectedLocation;

    public void setWaypoints(List<Locations> locations) {
        waypoints.clear();
        locations.forEach(loc
                -> waypoints.add(new DefaultWaypoint(
                        new GeoPosition(loc.latitude(), loc.longitude()))));
    }

    // Getters/Setters
    public Set<Waypoint> getWaypoints() {
        return waypoints;
    }

    public List<GeoPosition> getRoutePath() {
        return routePath;
    }

    public void setRoutePath(List<GeoPosition> path) {
        this.routePath = path;
    }

    public GeoPosition getCenter() {
        return center;
    }

    public void setCenter(GeoPosition center) {
        this.center = center;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public Locations getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Locations loc) {
        this.selectedLocation = loc;
    }
}
