package com.coffeecode.gui.handlers;

import java.util.List;

import javax.swing.SwingUtilities;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.gui.models.GraphPanelModel;
import com.coffeecode.model.Locations;
import com.coffeecode.service.DistanceService;

public class GraphPanelHandler {

    private static final Logger logger = LoggerFactory.getLogger(GraphPanelHandler.class);
    private static final double ZOOM_FACTOR = 0.1;

    private final GraphPanelModel model;
    private final DistanceService distanceService;
    private Viewer viewer;

    public GraphPanelHandler(GraphPanelModel model, DistanceService distanceService) {
        this.model = model;
        this.distanceService = distanceService;
    }

    public void updateLocations(List<Locations> locations) {
        SwingUtilities.invokeLater(() -> {
            try {
                logger.debug("Updating graph with {} locations", locations.size());
                model.getGraph().clear();

                // First add all nodes
                locations.forEach(loc -> addNode(loc));

                // Then add edges using same ID format
                for (int i = 0; i < locations.size(); i++) {
                    for (int j = i + 1; j < locations.size(); j++) {
                        addEdge(locations.get(i), locations.get(j));
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to update locations", e);
            }
        });
    }

    private Node addNode(Locations loc) {
        // Use short ID format
        String nodeId = loc.id().toString().split("-")[0];
        Node node = model.getGraph().addNode(nodeId);
        
        double x = transformLongitude(loc.longitude());
        double y = transformLatitude(loc.latitude());
        node.setAttribute("xy", x, y);
        node.setAttribute("ui.label", loc.name());
        
        return node;
    }

    private void addEdge(Locations loc1, Locations loc2) {
        // Use same ID format as nodes
        String id1 = loc1.id().toString().split("-")[0];
        String id2 = loc2.id().toString().split("-")[0];
        String edgeId = String.format("%s-%s", id1, id2);

        var edge = model.getGraph().addEdge(edgeId, id1, id2, false);
        double distance = distanceService.calculateDistance(loc1, loc2);
        edge.setAttribute("ui.label", String.format("%.1f km", distance));
    }

    private double transformLongitude(double lon) {
        return (lon + 180) / 360.0 * 2 - 1;
    }

    private double transformLatitude(double lat) {
        return (lat + 90) / 180.0 * 2 - 1;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void zoom(boolean in) {
        if (viewer != null) {
            double viewPercent = viewer.getDefaultView().getCamera().getViewPercent();
            double newPercent = in
                    ? viewPercent * (1 - ZOOM_FACTOR)
                    : viewPercent * (1 + ZOOM_FACTOR);
            viewer.getDefaultView().getCamera().setViewPercent(newPercent);
        }
    }

    public void resetView() {
        if (viewer != null) {
            viewer.getDefaultView().getCamera().resetView();
        }
    }
}
