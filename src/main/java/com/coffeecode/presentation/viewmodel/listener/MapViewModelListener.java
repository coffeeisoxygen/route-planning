package com.coffeecode.presentation.viewmodel.listener;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.view.components.MapComponent;
import com.coffeecode.presentation.viewmodel.base.Observer;
import com.coffeecode.presentation.viewmodel.state.MapState;

import java.util.List;

public class MapViewModelListener implements Observer {

    private final MapComponent mapComponent;

    public MapViewModelListener(MapComponent mapComponent) {
        this.mapComponent = mapComponent;
    }


    @Override
    public void onLocationSelected(Locations location) {
        mapComponent.centerOnLocation(location);
    }

    @Override
    public void onMapStateChanged(MapState state) {
        mapComponent.updateMapState(state);
    }

    @Override
    public void onError(String message) {
        mapComponent.showError(message);
    }
}
