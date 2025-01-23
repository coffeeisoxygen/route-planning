package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.domain.location.util.DistanceCalculator;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.service.LocationServiceImpl;
import com.coffeecode.application.service.RouteServiceImpl;

@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.application.service"
})
public class ModelConfig {

    @Bean
    public RouteMap routeMap(DistanceCalculator calculator) {
        return new RouteMap(calculator);
    }

    @Bean
    public LocationServiceImpl locationService(
            LocationPersistancePort persistancePort,
            RouteMap routeMap) {
        return new LocationServiceImpl(persistancePort, routeMap);
    }

    @Bean
    public RouteServiceImpl routeService(RouteMap routeMap) {
        return new RouteServiceImpl(routeMap);
    }
}
