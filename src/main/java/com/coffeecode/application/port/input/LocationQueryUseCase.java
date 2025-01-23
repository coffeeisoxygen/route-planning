package com.coffeecode.application.port.input;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.coffeecode.domain.location.model.Locations;

public interface LocationQueryUseCase {

    List<Locations> getAllLocations();

    Optional<Locations> findById(UUID id);

    Optional<Locations> findNearestLocation(double lat, double lon);
}
