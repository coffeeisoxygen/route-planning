package com.coffeecode.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.coffeecode.model.Locations;

public interface LocationRepository {

    Locations save(Locations location);

    Optional<Locations> findById(UUID id);

    Optional<Locations> findByName(String name);

    List<Locations> findAll();

    void delete(UUID id);

    boolean exists(UUID id);
}
