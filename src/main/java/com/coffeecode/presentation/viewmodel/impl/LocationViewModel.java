package com.coffeecode.presentation.viewmodel.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.coffeecode.application.port.input.LocationManagementUseCase;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.viewmodel.base.AbstractViewModel;
import com.coffeecode.presentation.viewmodel.state.LocationState;

@Component
public class LocationViewModel extends AbstractViewModel {

    private static final Logger logger = LoggerFactory.getLogger(LocationViewModel.class);
    private final LocationManagementUseCase locationUseCase;
    private LocationState state;

    public LocationViewModel(LocationManagementUseCase locationUseCase) {
        this.locationUseCase = locationUseCase;
        this.state = new LocationState(new ArrayList<>(), null);
    }

    @PostConstruct
    public void init() {
        loadLocations();
    }

    public void loadLocations() {
        try {
            List<Locations> locations = new ArrayList<>(locationUseCase.getAllLocations());
            updateState(new LocationState(locations, state.selectedLocation()));
        } catch (Exception e) {
            handleError(e);
        }
    }

    public void selectLocation(Locations location) {
        try {
            updateState(new LocationState(state.locations(), location));
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void updateState(LocationState newState) {
        state = newState;
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        notifyObservers(() -> observers.forEach(observer
                -> observer.onLocationStateChanged(state)));
    }

    @Override
    protected void handleError(Exception e) {
        logger.error("Error in LocationViewModel", e);
        observers.forEach(o -> o.onError(e.getMessage()));
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
