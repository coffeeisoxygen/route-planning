package com.coffeecode.domain.algorithm.result;

public record ExecutionStatistics(
        int visitedNodes,
        long startTime,
        long endTime) {

    public long getDurationMs() {
        return endTime - startTime;
    }

    public static class Builder {

        private int visitedNodes;
        private long startTime;
        private long endTime;

        public Builder start() {
            this.startTime = System.currentTimeMillis();
            return this;
        }

        public Builder stop() {
            this.endTime = System.currentTimeMillis();
            return this;
        }

        public Builder incrementVisited() {
            this.visitedNodes++;
            return this;
        }

        public ExecutionStatistics build() {
            return new ExecutionStatistics(visitedNodes, startTime, endTime);
        }
    }
}
