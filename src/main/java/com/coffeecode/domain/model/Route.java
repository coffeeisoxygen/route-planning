package com.coffeecode.domain.model;

import java.util.UUID;

public record Route(
        UUID sourceId,
        UUID targetId,
        double distance,
        double weight,
        RouteStatus status,
        RouteMetadata metadata,
        RouteType type) {

    @Override
    public String toString() {
        return String.format("Route{sourceId=%s, targetId=%s, distance=%.2f, type=%s}",
                sourceId.toString().substring(0, 8), targetId.toString().substring(0, 8), distance, type);
    }

    public enum RouteType {
        DIRECT, // Direct distance
        CALCULATED  // Algorithm calculated
    }

    public enum RouteStatus {
        ACTIVE,
        INACTIVE,
        UNDER_MAINTENANCE
    }

    public record RouteMetadata(
            long createdAt,
            long lastUpdated,
            String description) {

    }

    public static class Builder {

        private UUID sourceId;
        private UUID targetId;
        private double distance;
        private double weight;
        private RouteStatus status = RouteStatus.ACTIVE;
        private RouteMetadata metadata;
        private RouteType type;

        public Builder sourceId(UUID sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public Builder targetId(UUID targetId) {
            this.targetId = targetId;
            return this;
        }

        public Builder distance(double distance) {
            this.distance = distance;
            this.weight = distance; // Default weight = distance
            return this;
        }

        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder status(RouteStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(RouteType type) {
            this.type = type;
            return this;
        }

        public Route build() {
            if (metadata == null) {
                metadata = new RouteMetadata(
                        System.currentTimeMillis(),
                        System.currentTimeMillis(),
                        ""
                );
            }
            return new Route(sourceId, targetId, distance, weight, status, metadata, type);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Constructor validation
    public Route       {
        if (sourceId == null) {
            throw new IllegalArgumentException("Source ID cannot be null");
        }
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("Source and target cannot be the same");
        }
    }

    // Factory methods
    public static Route create(UUID sourceId, UUID targetId, double distance, RouteType type) {
        return builder()
                .sourceId(sourceId)
                .targetId(targetId)
                .distance(distance)
                .type(type)
                .build();
    }

    public static Route[] createBidirectional(UUID sourceId, UUID targetId, double distance, RouteType type) {
        return new Route[]{
            create(sourceId, targetId, distance, type),
            create(targetId, sourceId, distance, type)
        };
    }

    public boolean isActive() {
        return status == RouteStatus.ACTIVE;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public UUID getTargetId() {
        return targetId;
    }
}
