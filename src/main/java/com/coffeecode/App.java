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

            // Create
            Locations jakarta = locationService.addLocation("Jakarta", -6.200000, 106.816666);
            Locations bandung = locationService.addLocation("Bandung", -6.914744, 107.609810);
            //logger.info("Added locations: {} and {}", jakarta, bandung);

            // Calculate distance
            double distance = locationService.calculateDistance(jakarta.id(), bandung.id());
            logger.info("Distance between Jakarta and Bandung: {} km", String.format("%.2f", distance));

            // Update
            Locations updatedJakarta = locationService.updateLocation(
                    jakarta.id(),
                    "Jakarta Pusat",
                    -6.186486,
                    106.834091
            );
            logger.info("Updated location: {}", updatedJakarta);

            // Read
            logger.info("All locations: {}", locationService.getAllLocations());

            // Delete
            locationService.deleteLocation(jakarta.id());
            logger.info("After deletion: {}", locationService.getAllLocations());
        }
    }
}
