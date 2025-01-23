package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.domain.location.util.DistanceCalculator;
import com.coffeecode.domain.location.util.GeoToolsCalculator;
import com.coffeecode.infrastructure.persistance.InMemoryLocationRepository;

@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.infrastructure.persistance",
    "com.coffeecode.infrastructure.distance"
})
@Import(ModelConfig.class)
public class BackendConfig {

    @Bean
    @Primary
    public DistanceCalculator distanceCalculator() {
        return new GeoToolsCalculator();
    }

    @Bean
    public LocationPersistancePort locationRepository(DistanceCalculator calculator) {
        return new InMemoryLocationRepository(calculator);
    }
}
