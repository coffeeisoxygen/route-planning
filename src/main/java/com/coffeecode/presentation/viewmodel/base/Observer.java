package com.coffeecode.presentation.viewmodel.base;

import java.util.List;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.viewmodel.state.LocationState;
import com.coffeecode.presentation.viewmodel.state.MapState;

public interface Observer {

    default void onLocationsChanged(List<Locations> locations) {
    }

    default void onLocationSelected(Locations location) {
    }

    default void onMapStateChanged(MapState state) {
    }

    default void onRouteCalculated(List<Locations> route) {
    }

    default void onLocationStateChanged(LocationState state) {
    }

    default void onError(String message) {

    }
}
