package com.coffeecode.application.port.input;

import java.util.Collection;
import java.util.UUID;

import com.coffeecode.domain.model.Locations;

public interface LocationManagementUseCase {

    Locations addLocation(String name, double lat, double lon);

    void deleteLocation(UUID id);

    void updateLocation(UUID id, String name, double lat, double lon);

    Collection<Locations> getAllLocations();
}
