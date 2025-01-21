package com.coffeecode.gui.handlers;

import java.util.List;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.models.GraphPanelModel;
import com.coffeecode.model.Locations;
import com.coffeecode.service.DistanceService;

@Component
public class GraphPanelHandler {

    private final GraphPanelModel model;
    private final DistanceService distanceService;
    private Viewer viewer;

    public GraphPanelHandler(GraphPanelModel model, DistanceService distanceService) {
        this.model = model;
        this.distanceService = distanceService;
    }

    public void updateLocations(List<Locations> locations) {
        model.getGraph().clear();

        // Add nodes
        for (Locations loc : locations) {
            Node node = model.getGraph().addNode(loc.id().toString());
            node.setAttribute("ui.label", loc.name());
            node.setAttribute("xy", loc.longitude(), loc.latitude());
        }

        // Add edges
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                addEdge(locations.get(i), locations.get(j));
            }
        }
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
