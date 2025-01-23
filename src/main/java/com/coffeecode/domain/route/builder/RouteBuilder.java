package com.coffeecode.domain.route.builder;

import java.util.UUID;
import com.coffeecode.domain.route.model.*;

public class RouteBuilder {

    private UUID sourceId;

    private UUID targetId;
    private double distance;
    private double weight;
    private RouteStatus status = RouteStatus.ACTIVE;
    private RouteMetadata metadata;
    private RouteType type;

    public RouteBuilder sourceId(UUID sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public RouteBuilder targetId(UUID targetId) {
        this.targetId = targetId;
        return this;
    }

    public RouteBuilder distance(double distance) {
        this.distance = distance;
        return this;
    }

    public RouteBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }

    public RouteBuilder status(RouteStatus status) {

        this.status = status;
        return this;
    }

    public RouteBuilder metadata(RouteMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public RouteBuilder type(RouteType type) {
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
