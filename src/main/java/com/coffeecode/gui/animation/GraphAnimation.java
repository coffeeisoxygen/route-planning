package com.coffeecode.gui.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphAnimation implements ActionListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphAnimation.class);

    // Animation constants
    private static final int TIMER_DELAY = 50;  // 20 FPS
    private static final double ANIMATION_SPEED = 0.02;
    private static final double DEFAULT_RADIUS = 0.05;
    private static final double DEFAULT_JITTER = 0.01;
    private static final double DAMPING = 0.95;
    private static final double TRANSITION_SPEED = 0.01;

    private final Graph graph;
    private final Random random;
    private double time = 0;
    private boolean isPlaying = false;
    private Timer timer;
    private boolean isResetting = false;

    public GraphAnimation(Graph graph) {
        this.graph = graph;
        this.random = new Random();
        this.timer = new Timer(TIMER_DELAY, this);
    }

    public void start() {
        if (!isPlaying) {
            timer.start();
            isPlaying = true;
            logger.debug("Animation started");
        }
    }

    public void stop() {
        if (isPlaying) {
            timer.stop();
            isPlaying = false;
            logger.debug("Animation stopped");
        }
    }

    public void reset() {
        isResetting = true;
        time = 0;
        if (timer != null) {
            timer.stop();
        }
        resetNodePositions();
        isResetting = false;
    }

    private void resetNodePositions() {
        graph.nodes().forEach(node -> {
            double[] pos = getNodePosition(node);
            node.setAttribute("xy", pos[0], pos[1]);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time += ANIMATION_SPEED;
        updateNodePositions();
    }

    private void updateNodePositions() {
        graph.nodes().forEach(node -> {
            try {
                animateNode(node);
            } catch (Exception ex) {
                logger.error("Error animating node: {}", node.getId(), ex);
            }
        });
    }

    private double[] getNodePosition(Node node) {
        try {
            Object posObj = node.getAttribute("xy");
            if (posObj instanceof double[] ds) {
                return ds;
            } else if (posObj instanceof Object[] pos && pos.length >= 2) {
                return new double[]{
                    ((Number) pos[0]).doubleValue(),
                    ((Number) pos[1]).doubleValue()
                };
            } else {
                logger.warn("Invalid position format for node: {}", node.getId());
                return new double[]{0.0, 0.0};
            }
        } catch (Exception e) {
            logger.error("Error getting node position: {}", node.getId(), e);
        }
        return new double[]{0.0, 0.0}; // Default fallback
    }

    private void animateNode(Node node) {
        try {
            double[] pos = getNodePosition(node);
            double x = pos[0];
            double y = pos[1];

            // Calculate animation offsets
            double offsetX = Math.cos(time + node.getIndex() * Math.PI / 2) * DEFAULT_RADIUS;
            double offsetY = Math.sin(time + node.getIndex() * Math.PI / 2) * DEFAULT_RADIUS;

            // Add jitter
            offsetX += (random.nextDouble() - 0.5) * DEFAULT_JITTER;
            offsetY += (random.nextDouble() - 0.5) * DEFAULT_JITTER;

            // Update position using xy attribute
            node.setAttribute("xy", x + offsetX, y + offsetY);
        } catch (Exception e) {
            logger.error("Error animating node: {} - {}", node.getId(), e.getMessage());
        }
    }

    public void applyForce(Node node, double forceX, double forceY) {
        try {
            double[] pos = getNodePosition(node);
            node.setAttribute("xy",
                pos[0] + forceX * DAMPING,
                pos[1] + forceY * DAMPING
            );
        } catch (Exception e) {
            logger.error("Error applying force to node: {}", node.getId(), e);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
