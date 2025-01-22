package com.coffeecode.presentation.viewmodel.state;

import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

import com.coffeecode.domain.model.Locations;

// Consolidate notifications into MapState
public record MapState(
        GeoPosition center,
        int zoomLevel,
        List<Locations> displayedLocations,
        List<Locations> displayedRoute,
        Locations selectedLocation) {

}
