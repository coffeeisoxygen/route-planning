package com.coffeecode.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coffeecode.application.services.distance.DistanceCalculator;
import com.coffeecode.domain.model.Locations;

@Service
public class DistanceService {

    private final DistanceCalculator calculator;
    private static final Logger logger = LoggerFactory.getLogger(DistanceService.class);

    public DistanceService(DistanceCalculator calculator) {
        this.calculator = calculator;
    }

    public double calculateDistance(Locations loc1, Locations loc2) {
        if (loc1 == null || loc2 == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        logger.info("Calculating distance between {} and {} using {}",
                loc1.name(), loc2.name(), calculator.getStrategyName());

        return calculator.calculate(
                loc1.latitude(), loc1.longitude(),
                loc2.latitude(), loc2.longitude()
        );
    }

}
