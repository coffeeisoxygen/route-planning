package com.coffeecode.domain.algorithm.api;

import java.util.List;
import java.util.UUID;

import com.coffeecode.domain.algorithm.result.ExecutionStatistics;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

public interface PathFinding {

    List<Route> findPath(RouteMap map, UUID source, UUID target);

    ExecutionStatistics getLastRunStatistics();

    String getAlgorithmName();
}
