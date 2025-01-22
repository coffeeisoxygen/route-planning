package com.coffeecode.domain.model;

import java.util.UUID;

public record Route(
        UUID sourceId,
        UUID targetId,
        double distance,
        RouteType type) {

    public enum RouteType {
        DIRECT, // Direct distance
        CALCULATED  // Algorithm calculated
    }

    public Route    {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("Source and target must be different");
        }
    }

    // Create bidirectional routes
    public static Route[] createBidirectional(
            UUID source, UUID target, double distance, RouteType type) {
        return new Route[]{
            new Route(source, target, distance, type),
            new Route(target, source, distance, type)
        };
    }
}
