package com.coffeecode.gui.handlers;

import java.util.List;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.gui.animation.GraphAnimation;
import com.coffeecode.gui.models.GraphPanelModel;
import com.coffeecode.model.Locations;
import com.coffeecode.service.DistanceService;

public class GraphPanelHandler {

    private static final Logger logger = LoggerFactory.getLogger(GraphPanelHandler.class);

    private final GraphPanelModel model;
    private final DistanceService distanceService;
    private final GraphAnimation animation;
    private Viewer viewer;

    public GraphPanelHandler(GraphPanelModel model,
            DistanceService distanceService,
            GraphAnimation animation) {
        this.model = model;
        this.distanceService = distanceService;
        this.animation = animation;
    }

    public void updateLocations(List<Locations> locations) {
        logger.debug("Updating graph with {} locations", locations.size());

        if (viewer != null) {
            viewer.disableAutoLayout();
        }

        try {
            model.getGraph().clear();

            // Add nodes
            for (Locations loc : locations) {
                addNode(loc);
            }

            // Add edges
            for (int i = 0; i < locations.size(); i++) {
                for (int j = i + 1; j < locations.size(); j++) {
                    addEdge(locations.get(i), locations.get(j));
                }
            }

            if (model.isAutoLayoutEnabled() && viewer != null) {
                viewer.enableAutoLayout();
            }

            if (model.isAnimationEnabled()) {
                animation.start();
            }

        } catch (Exception e) {
            logger.error("Error updating graph", e);
        }
    }

    private void addNode(Locations loc) {
        String nodeId = loc.id().toString();
        Node node = model.getGraph().addNode(nodeId);

        // Scale coordinates
        double x = (loc.longitude() + 180) / 360.0 * 2 - 1;
        double y = (loc.latitude() + 90) / 180.0 * 2 - 1;

        node.setAttribute("xy", x, y);
        node.setAttribute("ui.label", loc.name());
    }

    private void addEdge(Locations loc1, Locations loc2) {
        String edgeId = loc1.id() + "-" + loc2.id();
        var edge = model.getGraph().addEdge(edgeId,
                loc1.id().toString(),
                loc2.id().toString(),
                false);

        double distance = distanceService.calculateDistance(loc1, loc2);
        edge.setAttribute("ui.label", String.format("%.2f km", distance));
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void zoom(boolean in) {
        if (viewer != null) {
            double viewPercent = viewer.getDefaultView().getCamera().getViewPercent();
            viewer.getDefaultView().getCamera().setViewPercent(
                    in ? viewPercent * 0.9 : viewPercent * 1.1);
        }
    }

    public void resetView() {
        if (viewer != null) {
            viewer.getDefaultView().getCamera().resetView();
        }
    }

    public void toggleAutoLayout() {
        model.setAutoLayoutEnabled(!model.isAutoLayoutEnabled());
        if (viewer != null) {
            if (model.isAutoLayoutEnabled()) {
                viewer.enableAutoLayout();
            } else {
                viewer.disableAutoLayout();
            }
        }
    }
}
