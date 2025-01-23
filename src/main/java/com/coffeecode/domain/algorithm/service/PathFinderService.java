package com.coffeecode.domain.algorithm.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.algorithm.result.PathResult;
import com.coffeecode.domain.algorithm.result.PathStatistics;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Service
public class PathFinderService {

    private final PathFinding algorithm;
    private final PathStatistics.Builder stats;

    public PathFinderService(PathFinding algorithm) {
        this.algorithm = algorithm;
        this.stats = new PathStatistics.Builder();
    }

    public PathResult execute(RouteMap map, UUID source, UUID target) {
        stats.startTimer();
        List<Route> path = algorithm.findPath(map, source, target);
        stats.stopTimer();

        return new PathResult(
                path,
                calculateTotalDistance(path),
                stats.build(),
                algorithm.getAlgorithmName()
        );
    }

    private double calculateTotalDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }
}
