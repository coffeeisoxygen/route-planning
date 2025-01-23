package com.coffeecode.infrastructure.persistance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.location.util.DistanceCalculator;

@ExtendWith(MockitoExtension.class)
class InMemoryLocationRepositoryTest {

    @Mock
    private DistanceCalculator calculator;
    private InMemoryLocationRepository repository;
    private Locations bandung;
    private Locations jakarta;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLocationRepository(calculator);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
    }

    @Test
    void save_shouldStoreAndReturnLocation() {
        Locations saved = repository.save(bandung);

        assertEquals(bandung, saved);
        assertTrue(repository.exists(bandung.id()));
    }

    @Test
    void findById_whenExists_shouldReturnLocation() {
        repository.save(bandung);

        Optional<Locations> found = repository.findById(bandung.id());

        assertTrue(found.isPresent());
        assertEquals(bandung, found.get());
    }

    @Test
    void findByName_whenExists_shouldReturnLocation() {
        repository.save(bandung);

        Optional<Locations> found = repository.findByName("Bandung");

        assertTrue(found.isPresent());
        assertEquals(bandung, found.get());
    }

    @Test
    void findAll_shouldReturnAllLocations() {
        repository.save(bandung);
        repository.save(jakarta);

        List<Locations> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(bandung));
        assertTrue(all.contains(jakarta));
    }

    @Test
    void delete_shouldRemoveLocation() {
        repository.save(bandung);
        repository.delete(bandung.id());

        assertFalse(repository.exists(bandung.id()));
        assertTrue(repository.findById(bandung.id()).isEmpty());
    }

    @Test
    void findNearestLocation_shouldReturnClosestLocation() {
        repository.save(bandung);
        repository.save(jakarta);

        when(calculator.calculateDistance(-6.3, 106.9, jakarta.latitude(), jakarta.longitude()))
                .thenReturn(50.0);
        when(calculator.calculateDistance(-6.3, 106.9, bandung.latitude(), bandung.longitude()))
                .thenReturn(100.0);

        Optional<Locations> nearest = repository.findNearestLocation(-6.3, 106.9);

        assertTrue(nearest.isPresent());
        assertEquals(jakarta, nearest.get());
    }

    @Test
    void findNearestLocation_whenEmpty_shouldReturnEmpty() {
        Optional<Locations> nearest = repository.findNearestLocation(0, 0);
        assertTrue(nearest.isEmpty());
    }
}
