package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.services.DistanceService;
import com.coffeecode.application.services.LocationService;
import com.coffeecode.application.services.distance.DistanceCalculator;
import com.coffeecode.application.services.distance.GeoToolsCalculator;

@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.service",
    "com.coffeecode.repository"
})
public class AppConfig {

    @Bean
    public DistanceCalculator distanceCalculator() {
        return new GeoToolsCalculator();
    }

    @Bean
    public DistanceService distanceService(DistanceCalculator calculator) {
        return new DistanceService(calculator);
    }

    @Bean
    public LocationService locationService(
            LocationPersistancePort locationRepository,
            DistanceService distanceService) {
        return new LocationService(locationRepository, distanceService);
    }
}
