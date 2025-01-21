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
        logger.debug("Updating graph with {} locations", locations.size());

        SwingUtilities.invokeLater(() -> {
            // Temporarily disable layout for batch updates
            model.getGraph().setAttribute("layout.frozen");

            model.getGraph().clear();

            // Add nodes with smooth positioning
            locations.forEach(loc -> {
                Node node = addNode(loc);
                // Add initial position for smooth transition
                double x = transformLongitude(loc.longitude());
                double y = transformLatitude(loc.latitude());
                node.setAttribute("xy", x, y);
            });

            // Add edges
            for (int i = 0; i < locations.size(); i++) {
                for (int j = i + 1; j < locations.size(); j++) {
                    addEdge(locations.get(i), locations.get(j));
                }
            }

            // Re-enable layout for animation
            model.getGraph().removeAttribute("layout.frozen");
        });
    }

    private Node addNode(Locations loc) {
        String nodeId = loc.id().toString();
        Node node = model.getGraph().addNode(nodeId);

        // Transform coordinates to graph space (-1 to 1 range)
        double x = transformLongitude(loc.longitude());
        double y = transformLatitude(loc.latitude());

        node.setAttribute("xy", x, y);
        node.setAttribute("ui.label", loc.name());
        
        return node;
    }

    private void addEdge(Locations loc1, Locations loc2) {
        String edgeId = String.format("%s-%s", loc1.id(), loc2.id());
        var edge = model.getGraph().addEdge(edgeId,
                loc1.id().toString(),
                loc2.id().toString(),
                false);

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
