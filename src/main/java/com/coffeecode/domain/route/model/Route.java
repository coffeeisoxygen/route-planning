package com.coffeecode.domain.route.model;

import java.util.UUID;
import com.coffeecode.domain.route.validation.RouteValidator;

public record Route(
        UUID sourceId,
        UUID targetId,
        double distance,
        double weight,
        RouteStatus status,
        RouteMetadata metadata,
        RouteType type) {

    public Route       {
        RouteValidator.validateRoute(sourceId, targetId, distance, weight);
    }

    public boolean isActive() {
        return status == RouteStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return String.format("Route{sourceId=%s, targetId=%s, distance=%.2f, type=%s}",
                sourceId.toString().substring(0, 8),
                targetId.toString().substring(0, 8),
                distance,
                type);
    }
}
