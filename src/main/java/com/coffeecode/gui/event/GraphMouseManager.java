package com.coffeecode.gui.event;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.gui.animation.GraphAnimation;

public class GraphMouseManager extends DefaultMouseManager implements ViewerListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphMouseManager.class);
    private static final double SELECTION_RADIUS = 0.01;
    private static final double FORCE_MULTIPLIER = 0.01;

    private final Graph graph;
    private final ViewerPipe viewerPipe;
    private final GraphAnimation animation;

    private Node selectedNode;
    private boolean dragging;
    private Point lastPoint;
    private Point3 currentPoint;  // Rename to avoid hiding

    public GraphMouseManager(Graph graph, ViewerPipe viewerPipe, GraphAnimation animation) {
        this.graph = graph;
        this.viewerPipe = viewerPipe;
        this.animation = animation;
    }

    @PostConstruct
    public void init() {
        this.viewerPipe.addViewerListener(this);
        logger.info("GraphMouseManager initialized and viewer listener added.");
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            View view = (View) event.getSource();
            Point3 p = view.getCamera().transformPxToGu(event.getX(), event.getY());

            selectedNode = findNodeAt(p.x, p.y);
            if (selectedNode != null) {
                dragging = true;
                lastPoint = event.getPoint();
                handleNodeSelection(selectedNode);
                logger.info("Node selected: {}", selectedNode.getId());
            } else {
                logger.info("No node found at position: ({}, {})", p.x, p.y);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (dragging && selectedNode != null) {
            View view = (View) event.getSource();
            currentPoint = view.getCamera().transformPxToGu(event.getX(), event.getY());

            // Calculate and apply force
            if (lastPoint != null) {
                double forceX = (event.getX() - lastPoint.x) * FORCE_MULTIPLIER;
                double forceY = (event.getY() - lastPoint.y) * FORCE_MULTIPLIER;
                
                // Update node position using force
                animation.applyForce(selectedNode, forceX, forceY);
                logger.debug("Force applied: ({}, {})", forceX, forceY);
            }

            lastPoint = event.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (selectedNode != null) {
            selectedNode.removeAttribute("ui.class");
            logger.info("Node deselected: {}", selectedNode.getId());
            selectedNode = null;
        }
        dragging = false;
        lastPoint = null;
    }

    private Node findNodeAt(double x, double y) {
        return graph.nodes()
                .filter(node -> isNodeAtPosition(node, x, y))
                .findFirst()
                .orElse(null);
    }

    private boolean isNodeAtPosition(Node node, double x, double y) {
        double[] pos = (double[]) node.getAttribute("xy");
        if (pos != null) {
            double dx = pos[0] - x;
            double dy = pos[1] - y;
            return (dx * dx + dy * dy) < SELECTION_RADIUS;
        }
        return false;
    }

    private void handleNodeSelection(Node node) {
        SwingUtilities.invokeLater(() -> {
            node.setAttribute("ui.class", "selected");
            node.setAttribute("ui.size", 20);
        });
    }

    // ViewerListener implementation
    @Override
    public void buttonPushed(String id) {
        logger.info("Button pushed: {}", id);
    }

    @Override
    public void buttonReleased(String id) {
        logger.info("Button released: {}", id);
    }

    @Override
    public void mouseOver(String id) {
        logger.info("Mouse over: {}", id);
    }

    @Override
    public void mouseLeft(String id) {
        logger.info("Mouse left: {}", id);
    }

    @Override
    public void viewClosed(String id) {
        viewerPipe.removeViewerListener(this);
        logger.info("View closed: {}", id);
    }
}
