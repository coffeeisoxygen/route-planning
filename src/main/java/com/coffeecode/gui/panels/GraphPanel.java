package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.handlers.GraphPanelHandler;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.model.Locations;

@Component
public class GraphPanel extends JPanel implements LocationTableModel.LocationTableListener {

    private static final Logger logger = LoggerFactory.getLogger(GraphPanel.class);

    private final Graph graph;
    private final SpringBox springLayout;
    private final GraphPanelHandler handler;
    private final LocationTableModel tableModel;
    private final DefaultMouseManager mouseManager;

    private SwingViewer viewer;
    private ViewerPipe fromViewer;
    private JToggleButton layoutToggle;

    @Autowired
    public GraphPanel(Graph graph,
            SpringBox springLayout,
            LocationTableModel tableModel,
            GraphPanelHandler handler,
            DefaultMouseManager mouseManager) {
        this.graph = graph;
        this.springLayout = springLayout;
        this.handler = handler;
        this.tableModel = tableModel;
        this.mouseManager = mouseManager;

        setLayout(new BorderLayout());
    }

    @PostConstruct
    public void initialize() {
        // Initialize viewer with proper threading
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        // Create view first
        View view = viewer.addDefaultView(false);
        view.setMouseManager(mouseManager);

        // Setup layout after view
        viewer.enableAutoLayout(springLayout);

        // Add components
        add(createToolbar(), BorderLayout.NORTH);
        add((JPanel) view, BorderLayout.CENTER);

        handler.setViewer(viewer);
        tableModel.addListener(this);

        logger.debug("GraphPanel initialized");
    }

    @PreDestroy
    public void cleanup() {
        if (viewer != null) {
            viewer.disableAutoLayout();
            viewer.close();
        }
        logger.debug("GraphPanel cleaned up");
    }

    private ViewerListener createViewerListener() {
        return new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {
                // Method intentionally left empty
            }

            @Override
            public void buttonPushed(String id) {
                logger.debug("Node clicked: {}", id);
            }

            @Override
            public void buttonReleased(String id) {
                // Method intentionally left empty
            }

            @Override
            public void mouseLeft(String id) {
                // Method intentionally left empty
            }

            @Override
            public void mouseOver(String id) {
                // Method intentionally left empty
            }
        };
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Layout toggle
        layoutToggle = new JToggleButton("Auto Layout", true);
        layoutToggle.addActionListener(e -> {
            if (layoutToggle.isSelected()) {
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

        // Add components
        toolbar.add(layoutToggle);
        toolbar.add(new JLabel("Zoom:"));
        toolbar.add(zoomIn);
        toolbar.add(zoomOut);
        toolbar.add(reset);

        return toolbar;
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
