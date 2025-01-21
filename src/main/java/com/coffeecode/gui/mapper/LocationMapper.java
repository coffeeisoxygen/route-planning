package com.coffeecode.gui.mapper;

import org.jxmapviewer.viewer.GeoPosition;
import com.coffeecode.domain.model.Locations;

public class LocationMapper {

    private LocationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GeoPosition toGeoPosition(Locations location) {
        return new GeoPosition(location.latitude(), location.longitude());
    }
}
