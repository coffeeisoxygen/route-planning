package com.coffeecode.domain.algorithm.api;

import java.util.UUID;

public interface SearchNode extends Comparable<SearchNode> {

    UUID getId();

    double getScore();

    @Override
    default int compareTo(SearchNode other) {
        return Double.compare(this.getScore(), other.getScore());
    }
}
