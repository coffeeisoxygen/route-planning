package com.coffeecode.gui.models;

import org.graphstream.graph.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphPanelModel {

    public static final String STYLESHEET = """
            graph { 
                padding: 50px;
            }
            node {
                size: 30px;
                fill-color: #4488FF, #FF4444;
                fill-mode: gradient-radial;
                stroke-mode: plain;
                stroke-color: #444444;
                stroke-width: 1px;
                text-size: 14px;
                text-style: bold;
                text-background-mode: rounded-box;
                text-padding: 5px;
                text-color: #000000;
            }
            node.selected {
                size: 40px;
                stroke-width: 3px;
                stroke-color: #CC0000;
            }
            node:hover { 
                stroke-width: 2px; 
            }
            edge {
                shape: line;
                fill-color: #666666;
                arrow-size: 10px, 5px;
            }
    """;
    private final Graph graph;

    @Autowired
    public GraphPanelModel(Graph graph) {
        this.graph = graph;
        graph.setAttribute("ui.stylesheet", STYLESHEET);
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.quality");
    }

    public Graph getGraph() {
        return graph;
    }
}
