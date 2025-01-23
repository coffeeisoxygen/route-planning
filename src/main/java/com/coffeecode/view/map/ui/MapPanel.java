package com.coffeecode.view.map.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.coffeecode.view.map.handler.MapHandler;
import com.coffeecode.view.map.handler.MapHandler.MapType;

public class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final transient MapHandler mapHandler;

    public MapPanel() {
        mapHandler = new MapHandler();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Add map viewer
        add(mapHandler.getMapViewer(), BorderLayout.CENTER);

        // Add overlay panel
        JPanel overlayPanel = createOverlayPanel();
        mapHandler.getMapViewer().setLayout(null); // Enable absolute positioning on the map viewer
        mapHandler.getMapViewer().add(overlayPanel);
        overlayPanel.setBounds(10, 10, 150, 30); // Position the overlay panel
    }

    private JPanel createOverlayPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make panel transparent
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Align content to the left

        JComboBox<MapHandler.MapType> mapTypeCombo = new JComboBox<>(MapHandler.MapType.values());
        mapTypeCombo.addActionListener(e
                -> mapHandler.switchMapType((MapType) mapTypeCombo.getSelectedItem())
        );

        panel.add(mapTypeCombo);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Map type selector
        JComboBox<MapHandler.MapType> mapTypeCombo = new JComboBox<>(MapHandler.MapType.values());
        mapTypeCombo.addActionListener(e -> 
            mapHandler.switchMapType((MapType) mapTypeCombo.getSelectedItem())
        );

        // Point control buttons
        JButton addPointBtn = new JButton("Add Points");
        addPointBtn.addActionListener(e -> {
            mapHandler.getWaypointHandler().toggleAddingPoints();
            addPointBtn.setText(addPointBtn.getText().equals("Add Points") ? "Stop Adding" : "Add Points");
        });

        JButton clearPointsBtn = new JButton("Clear Points");
        clearPointsBtn.addActionListener(e -> mapHandler.getWaypointHandler().clearWaypoints());

        panel.add(mapTypeCombo);
        panel.add(addPointBtn);
        panel.add(clearPointsBtn);
        
        return panel;
    }
}
