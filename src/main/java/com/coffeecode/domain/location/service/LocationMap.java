package com.coffeecode.domain.location.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.coffeecode.domain.exception.DuplicateLocationException;
import com.coffeecode.domain.location.model.Locations;

/*
 * this class is Location Management
 */
public class LocationMap {

    private final Map<UUID, Locations> locations;

    public LocationMap() {
        this.locations = new HashMap<>();
    }

    public void addLocation(Locations location) {
        if (locations.containsKey(location.id())) {
            throw new DuplicateLocationException("Location already exists: " + location.id());
        }
        locations.put(location.id(), location);
    }

    public Optional<Locations> getLocation(UUID id) {
        return Optional.ofNullable(locations.get(id));
    }

    // get all locations
    public Collection<Locations> getLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public boolean hasLocation(UUID id) {
        return locations.containsKey(id);
    }

    public Collection<Locations> getAllLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public void removeLocation(UUID id) {
        locations.remove(id);
    }

    public void clear() {
        locations.clear();
    }
}
