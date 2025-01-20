package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.repository.LocationRepository;
import com.coffeecode.service.DistanceService;
import com.coffeecode.service.LocationService;

@Configuration
@ComponentScan(basePackages = "com.coffeecode")
public class AppConfig {

    @Bean
    public LocationService locationService(LocationRepository locationRepository, DistanceService distanceService) {
        return new LocationService(locationRepository, distanceService);
    }

    @Bean
    public DistanceService distanceService() {
        return new DistanceService();
    }
}
