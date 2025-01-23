package com.coffeecode.application.port.input;

import java.util.UUID;

import com.coffeecode.domain.location.model.Locations;

public interface LocationManagementUseCase {

    Locations addLocation(String name, double lat, double lon);

    void deleteLocation(UUID id);

    void updateLocation(UUID id, String name, double lat, double lon);
}
