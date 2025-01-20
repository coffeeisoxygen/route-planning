package com.coffeecode.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.model.Locations;

class InMemoryLocationRepositoryTest {

    private InMemoryLocationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLocationRepository();
    }

    @Test
    void save_NewLocation_StoresLocation() {
        Locations location = new Locations("Jakarta", -6.200000, 106.816666);
        Locations saved = repository.save(location);

        assertTrue(repository.exists(saved.id()));
        assertEquals(location, saved);
    }

    @Test
    void findByName_ExistingLocation_ReturnsLocation() {
        Locations location = new Locations("Jakarta", -6.200000, 106.816666);
        repository.save(location);

        var found = repository.findByName("Jakarta");

        assertTrue(found.isPresent());
        assertEquals("Jakarta", found.get().name());
    }

    @Test
    void delete_ExistingLocation_RemovesLocation() {
        Locations location = new Locations("Jakarta", -6.200000, 106.816666);
        Locations saved = repository.save(location);

        repository.delete(saved.id());

        assertFalse(repository.exists(saved.id()));
    }
}
