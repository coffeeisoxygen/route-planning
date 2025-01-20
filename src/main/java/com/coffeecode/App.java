package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.config.AppConfig;
import com.coffeecode.model.Locations;
import com.coffeecode.service.LocationService;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            logger.info("Application context loaded successfully");

            LocationService locationService = context.getBean(LocationService.class);

            // Test CRUD operations
            // Create
            Locations jakarta = locationService.addLocation("Jakarta", -6.200000, 106.816666);
            Locations bandung = locationService.addLocation("Bandung", -6.914744, 107.609810);
            logger.info("Added locations: {} and {}", jakarta, bandung);

            // Read
            logger.info("All locations: {}", locationService.getAllLocations());

            // Find by name
            locationService.findByName("Jakarta")
                    .ifPresent(loc -> logger.info("Found Jakarta: {}", loc));

            // Delete
            locationService.deleteLocation(jakarta.id());
            logger.info("After deletion: {}", locationService.getAllLocations());
        }
    }
}
