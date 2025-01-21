package com.coffeecode.gui.models;

import org.graphstream.graph.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphPanelModel {

    private final Graph graph;

    @Autowired
    public GraphPanelModel(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    // Default styling
    public static final String DEFAULT_STYLE = """
        node {
            size: 15px;
            fill-color: rgb(0,100,255), rgb(255,0,0);
            fill-mode: gradient-radial;
            stroke-mode: plain;
            stroke-color: #555;
            text-size: 14;
            text-style: bold;
            text-background-mode: rounded-box;
            text-padding: 2px;
        }
        node.selected {
            size: 20px;
            fill-color: rgb(255,100,100), rgb(255,0,0);
            stroke-width: 3px;
        }
        edge {
            size: 2px;
            fill-color: #999;
            text-size: 12;
            text-background-mode: rounded-box;
            text-padding: 2px;
        }
    """;

    // Layout settings
    private static final double DEFAULT_FORCE = 0.5;
    private static final double DEFAULT_QUALITY = 0.9;
    private static final int DEFAULT_ZOOM = 1;

    // State
    private boolean autoLayoutEnabled = true;
    private boolean animationEnabled = true;
    private double zoomLevel = DEFAULT_ZOOM;
    private double layoutForce = DEFAULT_FORCE;
    private double layoutQuality = DEFAULT_QUALITY;

    // Getters and setters
    public boolean isAutoLayoutEnabled() {
        return autoLayoutEnabled;
    }

    public void setAutoLayoutEnabled(boolean enabled) {
        this.autoLayoutEnabled = enabled;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean enabled) {
        this.animationEnabled = enabled;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double level) {
        this.zoomLevel = level;
    }

    public double getLayoutForce() {
        return layoutForce;
    }

    public void setLayoutForce(double force) {
        this.layoutForce = force;
    }

    public double getLayoutQuality() {
        return layoutQuality;
    }

    public void setLayoutQuality(double quality) {
        this.layoutQuality = quality;
    }
}
