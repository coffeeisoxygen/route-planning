package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.*;
import com.coffeecode.domain.algorithm.api.ShortestPathStrategy;
import com.coffeecode.domain.algorithm.result.*;
import com.coffeecode.domain.graph.Graph;
import com.coffeecode.domain.route.model.Route;

public abstract class AbstractShortestPath implements ShortestPathStrategy {

    protected final ExecutionStatistics.Builder stats;

    protected AbstractShortestPath() {
        this.stats = new ExecutionStatistics.Builder();
    }

    @Override
    public PathResult findShortestPath(Graph graph, UUID source, UUID target) {
        stats.start();
        List<Route> path = executeShortestPath(graph, source, target);
        stats.stop();

        return new PathResult(
                path,
                calculateDistance(path),
                stats.build(),
                getStrategyName()
        );
    }

    protected abstract List<Route> executeShortestPath(Graph graph, UUID source, UUID target);

    protected double calculateDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }
}
