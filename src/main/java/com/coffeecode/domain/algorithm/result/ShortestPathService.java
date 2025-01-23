package com.coffeecode.domain.algorithm.result;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteMap;

@Service
public class ShortestPathService {

    private final PathFinding algorithm;

    public ShortestPathService(PathFinding algorithm) {
        this.algorithm = algorithm;
    }

    public ShortestPathResult findShortestPath(RouteMap map, UUID source, UUID target) {
        validateInput(map, source, target);

        try {
            List<Route> path = algorithm.findPath(map, source, target);
            return new ShortestPathResult(
                    path,
                    calculateTotalDistance(path),
                    algorithm.getLastRunStatistics(),
                    algorithm.getAlgorithmName()
            );
        } catch (Exception e) {
            throw new PathFindingException("Failed to find shortest path", e);
        }
    }

    private void validateInput(RouteMap map, UUID source, UUID target) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }

        if (!map.hasLocation(source)) {
            throw new IllegalArgumentException("Source not in map");
        }
        if (!map.hasLocation(target)) {
            throw new IllegalArgumentException("Target not in map");
        }
    }

    private double calculateTotalDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }
}
