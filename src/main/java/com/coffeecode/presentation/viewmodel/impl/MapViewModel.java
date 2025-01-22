package com.coffeecode.presentation.viewmodel.impl;

import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.viewmodel.base.AbstractViewModel;
import com.coffeecode.presentation.viewmodel.state.MapState;

@Component
public class MapViewModel extends AbstractViewModel {
    private static final Logger logger = LoggerFactory.getLogger(MapViewModel.class);
    private MapState state;

    public MapViewModel() {
        this.state = new MapState(
            new GeoPosition(-6.914744, 107.609810), // Bandung center
            8, // Default zoom
            List.of(), // Empty locations
            List.of(), // Empty route
            null // No selection
        );
    }

    public void setCenter(GeoPosition center) {
        updateState(new MapState(
            center,
            state.zoomLevel(),
            state.displayedLocations(),
            state.displayedRoute(),
            state.selectedLocation()
        ));
    }

    public void setZoom(int level) {
        updateState(new MapState(
            state.center(),
            level,
            state.displayedLocations(),
            state.displayedRoute(),
            state.selectedLocation()
        ));
    }

    public void updateDisplayedLocations(List<Locations> locations) {
        updateState(new MapState(
            state.center(),
            state.zoomLevel(),
            locations,
            state.displayedRoute(),
            state.selectedLocation()
        ));
    }

    public void updateDisplayedRoute(List<Locations> route) {
        updateState(new MapState(
            state.center(),
            state.zoomLevel(),
            state.displayedLocations(),
            route,
            state.selectedLocation()
        ));
    }

    private void updateState(MapState newState) {
        state = newState;
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        notifyObservers(() -> observers.forEach(o -> o.onMapStateChanged(state)));
    }

    @Override
    protected void handleError(Exception e) {
        logger.error("Error in map view model", e);
        observers.forEach(o -> o.onError(e.getMessage()));
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}