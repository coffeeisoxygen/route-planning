package com.coffeecode.domain.algorithm.api;

import java.util.Map;
import java.util.UUID;

public interface SingleSourceShortestPath {

    Map<UUID, UUID> getPredecessors();

    void initialize(UUID source);

}
