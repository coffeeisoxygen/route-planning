package com.coffeecode.domain.algorithm.api;

import java.util.List;
import java.util.UUID;

import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

public interface PathFinding {

    List<Route> findPath(RouteMap map, UUID source, UUID target);

    String getAlgorithmName();
}
