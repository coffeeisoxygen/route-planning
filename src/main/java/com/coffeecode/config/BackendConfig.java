package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.port.output.RouteCalculationPort;
import com.coffeecode.application.service.LocationServiceImpl;
import com.coffeecode.infrastructure.distance.GeoToolsRouteCalculator;

@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.application.service",
    "com.coffeecode.infrastructure.persistance", // Add specific packages
    "com.coffeecode.infrastructure.distance" // Add specific packages
})
public class BackendConfig {

    @Bean
    public RouteCalculationPort routeCalculator() {
        return new GeoToolsRouteCalculator();
    }

    @Bean
    public LocationServiceImpl locationService(
            LocationPersistancePort persistancePort,
            RouteCalculationPort routeCalculator) {
        return new LocationServiceImpl(persistancePort, routeCalculator);
    }
}
