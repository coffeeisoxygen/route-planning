package com.coffeecode.domain.algorithm.api;

import java.util.Map;
import java.util.UUID;

public interface AllPairsShortestPath extends ShortestPathFinding {

    Map<UUID, Map<UUID, Double>> getAllDistances();

    Map<UUID, Map<UUID, UUID>> getNextHops();
}
