package com.coffeecode.gui.event;

import java.util.List;

import com.coffeecode.domain.model.Locations;

public interface LocationUpdateListener {

    void onLocationsUpdated(List<Locations> locations);

    void onLocationSelected(Locations location);

    void onPathCalculated(Locations start, Locations end, List<Locations> path);
}
