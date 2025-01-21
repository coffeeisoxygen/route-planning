package com.coffeecode.gui.models;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.springframework.stereotype.Component;

@Component
public class GraphPanelModel {

    private final Graph graph;
    private boolean autoLayoutEnabled;
    private double zoomLevel;

    public static final String DEFAULT_STYLE = """
        node {
            size: 15px;
            fill-color: #3498db;
            text-size: 14;
            text-color: #2c3e50;
            text-style: bold;
        }
        edge {
            size: 2px;
            fill-color: #95a5a6;
            text-size: 12;
            text-color: #34495e;
        }
        """;

    public GraphPanelModel() {
        this.graph = new SingleGraph("Locations");
        this.autoLayoutEnabled = true;
        this.zoomLevel = 1.0;
        initGraph();
    }

    private void initGraph() {
        graph.setAttribute("ui.stylesheet", DEFAULT_STYLE);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
    }

    public Graph getGraph() {
        return graph;
    }

    public boolean isAutoLayoutEnabled() {
        return autoLayoutEnabled;
    }

    public void setAutoLayoutEnabled(boolean enabled) {
        this.autoLayoutEnabled = enabled;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double level) {
        this.zoomLevel = level;
    }
}
