package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.*;
import com.coffeecode.domain.algorithm.api.PathFindingStrategy;
import com.coffeecode.domain.algorithm.result.*;
import com.coffeecode.domain.graph.Graph;
import com.coffeecode.domain.route.model.Route;

public abstract class AbstractPathFinding implements PathFindingStrategy {

    protected final ExecutionStatistics.Builder stats;

    protected AbstractPathFinding() {
        this.stats = new ExecutionStatistics.Builder();
    }

    @Override
    public PathResult findPath(Graph graph, UUID source, UUID target) {
        stats.start();
        List<Route> path = executeFindPath(graph, source, target);
        stats.stop();

        return new PathResult(
                path,
                calculateDistance(path),
                stats.build(),
                getStrategyName()
        );
    }

    protected abstract List<Route> executeFindPath(Graph graph, UUID source, UUID target);

    protected double calculateDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }
}
