package com.coffeecode.gui.event;

import java.awt.event.MouseEvent;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphMouseManager extends DefaultMouseManager implements ViewerListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphMouseManager.class);
    private final Graph graph;
    private final ViewerPipe viewerPipe;
    private Node selectedNode;
    private boolean dragging;

    public GraphMouseManager(Graph graph, ViewerPipe viewerPipe) {
        this.graph = graph;
        this.viewerPipe = viewerPipe;
        this.viewerPipe.addViewerListener(this);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            View view = (View) event.getSource();
            Point3 p = view.getCamera().transformPxToGu(event.getX(), event.getY());

            // Find closest node
            selectedNode = graph.nodes()
                    .filter(node -> isNodeAtPosition(node, p.x, p.y))
                    .findFirst()
                    .orElse(null);

            if (selectedNode != null) {
                dragging = true;
                selectedNode.setAttribute("ui.class", "selected");
                logger.debug("Selected node: {}", selectedNode.getId());
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (dragging && selectedNode != null) {
            View view = (View) event.getSource();
            Point3 p = view.getCamera().transformPxToGu(event.getX(), event.getY());
            selectedNode.setAttribute("xy", p.x, p.y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (selectedNode != null) {
            selectedNode.removeAttribute("ui.class");
            selectedNode = null;
        }
        dragging = false;
    }

    private boolean isNodeAtPosition(Node node, double x, double y) {
        double[] pos = (double[]) node.getAttribute("xy");
        if (pos != null) {
            double dx = pos[0] - x;
            double dy = pos[1] - y;
            return (dx * dx + dy * dy) < 0.01; // Adjust radius as needed
        }
        return false;
    }

    // ViewerListener implementation
    @Override
    public void buttonPushed(String id) {
    }

    @Override
    public void buttonReleased(String id) {
    }

    @Override
    public void mouseOver(String id) {
    }

    @Override
    public void mouseLeft(String id) {
    }

    @Override
    public void viewClosed(String id) {
        viewerPipe.removeViewerListener(this);
    }
}
