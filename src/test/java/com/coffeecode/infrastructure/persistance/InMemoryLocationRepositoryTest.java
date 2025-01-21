package com.coffeecode.infrastructure.persistance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;

class InMemoryLocationRepositoryTest {

    private InMemoryLocationRepository repository;
    private Locations testLocation;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLocationRepository();
        testLocation = new Locations("Test Location", 0, 0);
    }

    @Test
    void shouldSaveAndRetrieveLocation() {
        Locations saved = repository.save(testLocation);
        assertEquals(testLocation, saved);

        var found = repository.findById(testLocation.id());
        assertTrue(found.isPresent());
        assertEquals(testLocation, found.get());
    }

    @Test
    void shouldFindAllLocations() {
        Locations loc1 = new Locations("Location 1", 0, 0);
        Locations loc2 = new Locations("Location 2", 1, 1);

        repository.save(loc1);
        repository.save(loc2);

        List<Locations> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(loc1));
        assertTrue(all.contains(loc2));
    }

    @Test
    void shouldFindNearestLocation() {
        Locations near = new Locations("Near", 0, 0);
        Locations far = new Locations("Far", 10, 10);

        repository.save(near);
        repository.save(far);

        var nearest = repository.findNearestLocation(0.1, 0.1);
        assertTrue(nearest.isPresent());
        assertEquals(near, nearest.get());
    }
}
