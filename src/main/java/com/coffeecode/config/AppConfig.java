package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.repository.LocationRepository;
import com.coffeecode.service.DistanceService;
import com.coffeecode.service.LocationService;
import com.coffeecode.util.distance.DistanceCalculator;
import com.coffeecode.util.distance.GeoToolsCalculator;

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
            LocationRepository locationRepository,
            DistanceService distanceService) {
        return new LocationService(locationRepository, distanceService);
    }
}
