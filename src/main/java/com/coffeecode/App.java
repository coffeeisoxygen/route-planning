package com.coffeecode;

import java.util.Collection;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.application.service.LocationServiceImpl;
import com.coffeecode.application.service.RouteServiceImpl;
import com.coffeecode.config.AppConfig;
import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.route.model.Route;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            // Create Spring context with both configs
            AnnotationConfigApplicationContext context
                    = new AnnotationConfigApplicationContext(AppConfig.class);

            // Run simulation
            runSimulation(context);

            SwingUtilities.invokeLater(() -> {
                logger.info("GUI initialized successfully");
            });

            logger.info("Application started successfully");

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                context.close();
                logger.info("Application shutdown complete");
            }));

        } catch (Exception e) {
            logger.error("Application failed to start", e);
            System.exit(1);
        }
    }

    private static void runSimulation(ApplicationContext context) {
        LocationServiceImpl locationService = context.getBean(LocationServiceImpl.class);
        RouteServiceImpl routeService = context.getBean(RouteServiceImpl.class);

        // Add test locations
        logger.info("Starting simulation...");

        Locations bandung = locationService.addLocation("Bandung", -6.914744, 107.609810);
        Locations jakarta = locationService.addLocation("Jakarta", -6.200000, 106.816666);
        Locations surabaya = locationService.addLocation("Surabaya", -7.250445, 112.768845);

        logger.info("Added locations: Bandung, Jakarta, Surabaya");

        // Get routes
        Route jakartaBandung = routeService.getRoute(jakarta.id(), bandung.id());
        Route jakartaSurabaya = routeService.getRoute(jakarta.id(), surabaya.id());

        logger.info("Route Jakarta-Bandung: {} km", jakartaBandung.distance());
        logger.info("Route Jakarta-Surabaya: {} km", jakartaSurabaya.distance());

        // Get all routes
        Collection<Route> allRoutes = routeService.getAllRoutes();
        logger.info("Total routes in system: {}", allRoutes.size());

        logger.info("Simulation completed successfully");
    }
}
