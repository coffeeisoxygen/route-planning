package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.model.Locations;
import com.coffeecode.service.DistanceService;

@Component
public class GraphPanel extends JPanel implements LocationTableModel.LocationTableListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphPanel.class);
    private final Graph graph;
    private final DistanceService distanceService;
    private final LocationTableModel tableModel;
    private Viewer viewer;

    static {
        System.setProperty("org.graphstream.ui", "swing");
    }

    @Autowired
    public GraphPanel(Graph graph, LocationTableModel tableModel, DistanceService distanceService) {
        this.graph = graph;
        this.distanceService = distanceService;
        this.tableModel = tableModel;

        // Register as listener for table changes
        tableModel.addListener(this);

        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Graph controls
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Initialize viewer correctly
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        View view = viewer.addDefaultView(false); // false indicates "no JFrame"
        add((JPanel) view, BorderLayout.CENTER);
    }

    @Override
    public void onLocationChanged(List<Locations> locations) {
        logger.info("Updating graph with {} locations", locations.size());
        SwingUtilities.invokeLater(() -> {
            try {
                updateGraph(locations);
            } catch (Exception e) {
                logger.error("Error updating graph", e);
            }
        });
    }

    private void updateGraph(List<Locations> locations) {
        graph.clear();
        logger.debug("Cleared graph");

        // Add nodes
        for (Locations loc : locations) {
            String nodeId = loc.id().toString();
            logger.debug("Adding node: {}", nodeId);

            Node node = graph.addNode(nodeId);
            node.setAttribute("xy", loc.longitude(), loc.latitude());
            node.setAttribute("ui.label", loc.name());
        }

        // Add edges
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                addEdge(locations.get(i), locations.get(j));
            }
        }

        // Force layout update
        viewer.enableAutoLayout();
    }

    private void addEdge(Locations loc1, Locations loc2) {
        String edgeId = loc1.id() + "-" + loc2.id();
        logger.debug("Adding edge: {}", edgeId);

        org.graphstream.graph.Edge edge = graph.addEdge(edgeId,
                loc1.id().toString(),
                loc2.id().toString(),
                false);

        double distance = distanceService.calculateDistance(loc1, loc2);
        edge.setAttribute("ui.label", String.format("%.2f km", distance));
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add zoom controls
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        JButton resetView = new JButton("Reset");
        JButton autoLayout = new JButton("Auto Layout");

        zoomIn.addActionListener(e -> zoomGraph(true));
        zoomOut.addActionListener(e -> zoomGraph(false));
        resetView.addActionListener(e -> resetGraphView());
        autoLayout.addActionListener(e -> toggleAutoLayout());

        panel.add(new JLabel("Zoom:"));
        panel.add(zoomIn);
        panel.add(zoomOut);
        panel.add(resetView);
        panel.add(autoLayout);

        return panel;
    }

    private void zoomGraph(boolean in) {
        if (viewer != null) {
            double viewPercent = viewer.getDefaultView().getCamera().getViewPercent();
            viewer.getDefaultView().getCamera().setViewPercent(
                    in ? viewPercent * 0.9 : viewPercent * 1.1);
        }
    }

    private void resetGraphView() {
        if (viewer != null) {
            viewer.getDefaultView().getCamera().resetView();
        }
    }

    private void toggleAutoLayout() {
        if (viewer != null) {
            if (graph.hasAttribute("layout.frozen")) {
                graph.removeAttribute("layout.frozen");
                viewer.enableAutoLayout();
            } else {
                graph.setAttribute("layout.frozen");
                viewer.disableAutoLayout();
            }
        }
    }

}
