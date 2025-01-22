package com.coffeecode.domain.algorithm.result;

import java.util.Collections;
import java.util.List;

import com.coffeecode.domain.algorithm.api.PathStatistics;
import com.coffeecode.domain.model.Route;

public record PathResult(
        List<Route> path,
        double totalDistance,
        PathStatistics statistics,
        String algorithmName) {

    public PathResult    {
        path = Collections.unmodifiableList(path);
    }
}
