package com.coffeecode.presentation.viewmodel;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.coffeecode.application.port.input.LocationManagementUseCase;
import com.coffeecode.domain.model.Locations;

@Component
public class LocationViewModel {

    private final LocationManagementUseCase locationUseCase;
    private final List<Locations> locations = new ArrayList<>();
    private Locations selectedLocation;
    private final List<ViewModelListener> listeners = new ArrayList<>();

    public interface ViewModelListener {

        void onLocationsChanged(List<Locations> locations);

        void onLocationSelected(Locations location);
    }

    public LocationViewModel(LocationManagementUseCase locationUseCase) {
        this.locationUseCase = locationUseCase;
    }

    public void addListener(ViewModelListener listener) {
        listeners.add(listener);
    }

    public void loadLocations() {
        locations.clear();
        locations.addAll(locationUseCase.getAllLocations());
        notifyLocationsChanged();
    }

    public void selectLocation(Locations location) {
        this.selectedLocation = location;
        listeners.forEach(l -> l.onLocationSelected(location));
    }

    private void notifyLocationsChanged() {
        listeners.forEach(l -> l.onLocationsChanged(locations));
    }
}
