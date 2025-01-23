package com.coffeecode.view.map;

public enum MapType {
    OPENSTREETMAP("OpenStreetMap"),
    SATELLITE("Satellite"),
    TERRAIN("Terrain"),
    HYBRID("Hybrid");

    private final String displayName;

    MapType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
