package com.coffeecode.domain.algorithm.api;

import java.util.Map;
import java.util.UUID;

public interface ShortestPathFinding extends PathFinding {

    Map<UUID, Double> getDistances();

    double getPathCost(UUID target);
}
