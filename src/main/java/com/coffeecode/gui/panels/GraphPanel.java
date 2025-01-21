package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.event.GraphMouseManager;
import com.coffeecode.gui.handlers.GraphPanelHandler;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.model.Locations;

@Component
public class GraphPanel extends JPanel implements LocationTableModel.LocationTableListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphPanel.class);

    private final Graph graph;
    private final GraphPanelHandler handler;
    private final GraphMouseManager mouseManager;
    private final SpringBox springLayout;
    private final SwingViewer viewer;
    private boolean enablePhysics = true;

    @Autowired
    public GraphPanel(Graph graph, SpringBox springLayout,
            LocationTableModel tableModel,
            GraphPanelHandler handler,
            GraphMouseManager mouseManager) {
        this.graph = graph;
        this.springLayout = springLayout;
        this.handler = handler;
        this.mouseManager = mouseManager;

        setLayout(new BorderLayout());

        // Initialize viewer
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout(springLayout);
        handler.setViewer(viewer);

        initComponents();
        tableModel.addListener(this);
    }

    private void initComponents() {
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        View view = viewer.addDefaultView(false);
        view.setMouseManager(mouseManager);
        add((JPanel) view, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Physics toggle
        JToggleButton physicsToggle = new JToggleButton("Physics", enablePhysics);
        physicsToggle.addActionListener(e -> {
            enablePhysics = physicsToggle.isSelected();
            if (enablePhysics) {
                viewer.enableAutoLayout(springLayout);
            } else {
                viewer.disableAutoLayout();
            }
        });

        // Zoom controls
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        JButton reset = new JButton("Reset");

        zoomIn.addActionListener(e -> handler.zoom(true));
        zoomOut.addActionListener(e -> handler.zoom(false));
        reset.addActionListener(e -> handler.resetView());

        panel.add(physicsToggle);
        panel.add(new JLabel("Zoom:"));
        panel.add(zoomIn);
        panel.add(zoomOut);
        panel.add(reset);

        return panel;
    }

    @Override
    public void onLocationChanged(List<Locations> locations) {
        SwingUtilities.invokeLater(() -> {
            try {
                handler.updateLocations(locations);
            } catch (Exception e) {
                logger.error("Error updating locations", e);
            }
        });
    }
}
