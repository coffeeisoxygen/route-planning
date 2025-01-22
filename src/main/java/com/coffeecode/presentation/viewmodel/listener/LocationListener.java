package com.coffeecode.presentation.viewmodel.listener;

import java.util.List;

import com.coffeecode.domain.model.Locations;

public interface LocationListener {

    void onLocationsChanged(List<Locations> locations);

    void onLocationSelected(Locations location);
}
