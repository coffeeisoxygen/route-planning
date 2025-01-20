package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.repository.InMemoryLocationRepository;
import com.coffeecode.repository.LocationRepository;
import com.coffeecode.service.LocationService;

@Configuration
@ComponentScan(basePackages = "com.coffeecode")
public class AppConfig {

    @Bean
    public LocationRepository locationRepository() {
        return new InMemoryLocationRepository();
    }

    @Bean
    public LocationService locationService(LocationRepository locationRepository) {
        return new LocationService(locationRepository);
    }
}
