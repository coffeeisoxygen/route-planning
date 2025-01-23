package com.coffeecode.domain.algorithm.api;

import java.util.*;
import com.coffeecode.domain.algorithm.result.PathResult;
import com.coffeecode.domain.graph.Graph;

public interface ShortestPathStrategy {

    PathResult findShortestPath(Graph graph, UUID source, UUID target);

    String getStrategyName();
}
