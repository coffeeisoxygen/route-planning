package com.coffeecode.domain.algorithm.result;

import java.util.*;
import com.coffeecode.domain.route.model.Route;

public record PathResult(
    List<Route> path,
    double totalDistance,
    ExecutionStatistics stats,
    String algorithmName
) {
    public PathResult {
        path = Collections.unmodifiableList(path);
    }
}