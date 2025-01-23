package com.coffeecode.domain.algorithm.result;

public record PathStatistics(
        int visitedNodes,
        long startTime,
        long endTime) {

    public long getExecutionTime() {
        return endTime - startTime;
    }

    public int getVisitedNodes() {
        return visitedNodes;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public static class Builder {

        private int visitedNodes;
        private long startTime;
        private long endTime;

        public void startTimer() {
            this.startTime = System.nanoTime();
        }

        public void stopTimer() {
            this.endTime = System.nanoTime();
        }

        public void incrementVisited() {
            this.visitedNodes++;
        }

        public PathStatistics build() {
            return new PathStatistics(visitedNodes, startTime, endTime);
        }
    }
}
