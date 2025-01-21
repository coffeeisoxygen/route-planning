package com.coffeecode.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.coffeecode.domain.model.Locations;

public interface LocationPersistancePort {

    Locations save(Locations location);

    Optional<Locations> findById(UUID id);

    Optional<Locations> findByName(String name);

    List<Locations> findAll();

    void delete(UUID id);

    boolean exists(UUID id);

    Optional<Locations> findNearestLocation(double lat, double lon);
}
