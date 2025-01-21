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

    // Add getters
    public List<Locations> getLocations() {
        return new ArrayList<>(locations); // Return copy for immutability
    }

    public Locations getSelectedLocation() {
        return selectedLocation;
    }

    public LocationManagementUseCase getLocationUseCase() {
        return locationUseCase;
    }

    public void addLocation(String name, double lat, double lon) {
        Locations newLocation = locationUseCase.addLocation(name, lat, lon);
        locations.add(newLocation);
        notifyLocationsChanged();
    }

    public void deleteLocation(Locations location) {
        locationUseCase.deleteLocation(location.id());
        locations.remove(location);
        if (selectedLocation == location) {
            selectedLocation = null;
        }
        notifyLocationsChanged();
    }

    public void updateLocation(Locations location, String newName) {
        locationUseCase.updateLocation(location.id(), newName, 
            location.latitude(), location.longitude());
        loadLocations(); // Reload to get updated data
    }
}
