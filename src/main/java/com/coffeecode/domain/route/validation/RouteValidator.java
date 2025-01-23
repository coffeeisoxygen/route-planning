package com.coffeecode.domain.route.validation;

import java.util.UUID;

import com.coffeecode.domain.route.exception.InvalidRouteException;

public final class RouteValidator {
    private RouteValidator() {}

    public static void validateRoute(UUID sourceId, UUID targetId, double distance, double weight) {
        validateIds(sourceId, targetId);
        validateMetrics(distance, weight);
    }

    private static void validateIds(UUID sourceId, UUID targetId) throws InvalidRouteException {
        if (sourceId == null) {
            throw new InvalidRouteException("Source ID cannot be null");
        }
        if (targetId == null) {
            throw new InvalidRouteException("Target ID cannot be null");
        }
        if (sourceId.equals(targetId)) {
            throw new InvalidRouteException("Source and target cannot be the same");
        }
    }

    private static void validateMetrics(double distance, double weight) throws InvalidRouteException {
        if (distance < 0) {
            throw new InvalidRouteException("Distance cannot be negative");
        }
        if (weight < 0) {
            throw new InvalidRouteException("Weight cannot be negative");
        }
    }
}