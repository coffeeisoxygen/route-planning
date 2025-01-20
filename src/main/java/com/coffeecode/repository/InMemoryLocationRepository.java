package com.coffeecode.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.coffeecode.model.Locations;

@Repository
@Primary
public class InMemoryLocationRepository implements LocationRepository {

    private final Map<UUID, Locations> locations = new HashMap<>();

    @Override
    public Locations save(Locations location) {
        locations.put(location.id(), location);
        return location;
    }

    @Override
    public Optional<Locations> findById(UUID id) {
        return Optional.ofNullable(locations.get(id));
    }

    @Override
    public Optional<Locations> findByName(String name) {
        return locations.values().stream()
                .filter(location -> location.name().equals(name))
                .findFirst();
    }

    @Override
    public List<Locations> findAll() {
        return new ArrayList<>(locations.values());
    }

    @Override
    public void delete(UUID id) {
        locations.remove(id);
    }

    @Override
    public boolean exists(UUID id) {
        return locations.containsKey(id);
    }
}
