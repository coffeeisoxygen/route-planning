package com.coffeecode.domain.algorithm.component;

import com.coffeecode.domain.algorithm.result.PathStatistics;

public class PathFindingStats {

    private PathStatistics lastRunStats;
    private final PathStatistics.Builder statsBuilder;

    public PathFindingStats() {
        this.statsBuilder = new PathStatistics.Builder();
    }

    public void startTracking() {
        statsBuilder.startTimer();
    }

    public void stopTracking() {
        statsBuilder.stopTimer();
        this.lastRunStats = statsBuilder.build();
    }

    public void incrementVisited() {
        statsBuilder.incrementVisited();
    }

    public PathStatistics getLastRunStats() {
        return lastRunStats;
    }
}
