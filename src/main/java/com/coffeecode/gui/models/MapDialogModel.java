package com.coffeecode.gui.models;

import java.util.Map;

import org.jxmapviewer.viewer.GeoPosition;
import org.springframework.stereotype.Component;

@Component
public class MapDialogModel {

    // Default map settings
    private static final int DEFAULT_ZOOM = 7;

    // Default locations for quick navigation
    public static final Map<String, GeoPosition> DEFAULT_LOCATIONS = Map.of(
            "Sukabumi", new GeoPosition(-6.91, 106.92),
            "Jakarta", new GeoPosition(-6.20, 106.81),
            "Bandung", new GeoPosition(-6.91, 107.60)
    );

    // Map state
    private GeoPosition currentPosition;
    private int currentZoom;
    private MapType currentMapType;

    public enum MapType {
        OPENSTREETMAP,
        VIRTUAL_EARTH
    }

    public MapDialogModel() {
        // Initialize with default values
        this.currentPosition = DEFAULT_LOCATIONS.get("Jakarta");
        this.currentZoom = DEFAULT_ZOOM;
        this.currentMapType = MapType.OPENSTREETMAP;
    }

    // Getters and setters
    public GeoPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GeoPosition position) {
        this.currentPosition = position;
    }

    public int getCurrentZoom() {
        return currentZoom;
    }

    public void setCurrentZoom(int zoom) {
        this.currentZoom = zoom;
    }

    public MapType getCurrentMapType() {
        return currentMapType;
    }

    public void setCurrentMapType(MapType mapType) {
        this.currentMapType = mapType;
    }
}
