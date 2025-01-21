package com.coffeecode.gui.event;

import java.util.List;

import com.coffeecode.model.Locations;

public interface LocationUpdateListener {

    void onLocationsUpdated(List<Locations> locations);

    void onLocationSelected(Locations location);

    void onRouteCalculated(Locations start, Locations end, double distance);
}
