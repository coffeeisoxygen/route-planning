package com.coffeecode.domain.route.factory;

import java.util.UUID;

import com.coffeecode.domain.route.builder.RouteBuilder;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteType;

public final class RouteFactory {

    private RouteFactory() {
    }

    public static Route create(UUID sourceId, UUID targetId, double distance, RouteType type) {
        return new RouteBuilder()
                .sourceId(sourceId)
                .targetId(targetId)
                .distance(distance)
                .type(type)
                .build();
    }

    public static Route[] createBidirectional(UUID sourceId, UUID targetId, double distance, RouteType type) {
        return new Route[]{
            create(sourceId, targetId, distance, type),
            create(sourceId, targetId, distance, type)
        };
    }
}
