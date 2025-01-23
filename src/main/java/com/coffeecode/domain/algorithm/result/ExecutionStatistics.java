package com.coffeecode.domain.algorithm.result;

public record ExecutionStatistics(int visitedNodes, long startTime, long endTime) {

    public long getExecutionTime() {
        return endTime - startTime;
    }

    public static class Tracker {

        private int visitedNodes;
        private long startTime;
        private long endTime;

        public void start() {
            startTime = System.nanoTime();
            visitedNodes = 0;
        }

        public void stop() {
            endTime = System.nanoTime();
        }

        public void incrementVisited() {
            visitedNodes++;
        }

        public ExecutionStatistics build() {
            return new ExecutionStatistics(visitedNodes, startTime, endTime);
        }
    }
}
