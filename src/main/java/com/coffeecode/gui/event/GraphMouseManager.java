package com.coffeecode.gui.event;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphMouseManager extends DefaultMouseManager {

    private static final Logger logger = LoggerFactory.getLogger(GraphMouseManager.class);
    private static final double SELECTION_DISTANCE = 0.01;

    private final Graph graph;
    private Node selectedNode;
    private Point2D.Double lastPosition;

    public GraphMouseManager(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        View view = (View) event.getSource();
        Point3 mousePosition = view.getCamera().transformPxToGu(event.getX(), event.getY());

        selectedNode = findNearestNode(mousePosition.x, mousePosition.y);
        if (selectedNode != null) {
            lastPosition = new Point2D.Double(event.getX(), event.getY());
            selectedNode.setAttribute("ui.class", "selected");
            logger.debug("Selected node: {}", selectedNode.getId());
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (selectedNode == null) {
            return;
        }

        View view = (View) event.getSource();
        Point3 newPos = view.getCamera().transformPxToGu(event.getX(), event.getY());
        selectedNode.setAttribute("xy", newPos.x, newPos.y);
        lastPosition = new Point2D.Double(event.getX(), event.getY());
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (selectedNode != null) {
            selectedNode.removeAttribute("ui.class");
            selectedNode = null;
            lastPosition = null;
        }
    }

    private Node findNearestNode(double x, double y) {
        return graph.nodes()
                .filter(node -> {
                    double[] pos = (double[]) node.getAttribute("xy");
                    if (pos != null) {
                        double dx = pos[0] - x;
                        double dy = pos[1] - y;
                        return (dx * dx + dy * dy) < SELECTION_DISTANCE;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }
}
