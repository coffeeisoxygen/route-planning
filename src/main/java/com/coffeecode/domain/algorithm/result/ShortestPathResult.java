package com.coffeecode.domain.algorithm.result;

import java.util.Collections;
import java.util.List;

import com.coffeecode.domain.route.model.Route;

public record ShortestPathResult(
        List<Route> path,
        double totalDistance,
        ExecutionStatistics stats,
        String algorithmName) {

    public ShortestPathResult    {
        path = Collections.unmodifiableList(path);
    }
}
