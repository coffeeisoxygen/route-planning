package com.coffeecode.domain.algorithm.component;

import java.util.Map;
import java.util.UUID;

public interface ShortestPathFinding extends PathFinding {

    Map<UUID, Double> getDistances();

    Map<UUID, UUID> getPath();  // Track complete path

    double getPathCost(UUID target); // Get specific path cost
}
