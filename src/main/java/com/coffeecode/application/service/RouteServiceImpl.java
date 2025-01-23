package com.coffeecode.application.service;

import java.util.Collection;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.coffeecode.domain.exception.RouteNotFoundException;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

@Service
@Primary
public class RouteServiceImpl {

    private final RouteMap routeMap;

    @Autowired
    public RouteServiceImpl(RouteMap routeMap) {
        this.routeMap = routeMap;
    }

    public Route getRoute(UUID from, UUID to) {
        return routeMap.getRoute(from, to)
                .orElseThrow(() -> new RouteNotFoundException("No route found"));
    }

    public Collection<Route> getAllRoutes() {
        return routeMap.getRoutes();
    }
}
