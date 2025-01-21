package com.coffeecode.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.MapViewController;
import com.coffeecode.gui.panels.GraphView;
import com.coffeecode.gui.panels.MapView;
import com.coffeecode.gui.panels.RouteControlPanel;
import com.coffeecode.gui.panels.StatisticsPanel;

@Component
public class MainFrame extends JFrame {

    private final JSplitPane mainSplitPane;
    private final JSplitPane rightSplitPane;
    private final MapView mapView;
    private final GraphView graphView;
    private final StatisticsPanel statsPanel;
    private final RouteControlPanel controlPanel;

    @Autowired
    public MainFrame(MapViewController controller) {
        setTitle("Route Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        mapView = new MapView(controller);
        graphView = new GraphView();
        statsPanel = new StatisticsPanel();
        controlPanel = new RouteControlPanel();

        // Setup split panes
        rightSplitPane = new JSplitPane();
        rightSplitPane.setResizeWeight(0.7);

        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mapView, rightSplitPane);
        mainSplitPane.setResizeWeight(0.3);

        // Layout
        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
}
