package com.coffeecode.view.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.coffeecode.view.map.handler.MapHandler;

public class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final transient MapHandler mapHandler;

    public MapPanel() {
        this.mapHandler = new MapHandler();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(mapHandler.getMapViewer(), BorderLayout.CENTER);

        // Add overlay control panel
        JPanel controlPanel = createControlPanel();
        mapHandler.getMapViewer().setLayout(null);
        mapHandler.getMapViewer().add(controlPanel);
        controlPanel.setBounds(10, 10, 250, 35); // Position at top-left, smaller control panel
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Full transparency for the control panel
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Adjust spacing between components

        // Set preferred size to make control panel smaller
        panel.setPreferredSize(new Dimension(350, 35));  // Increased panel width

        // Different sizes for combo and buttons
        Dimension comboSize = new Dimension(150, 25);    // Wider combo box
        Dimension buttonSize = new Dimension(90, 25);    // Slightly wider buttons

        // Map type selector with fixed size
        JComboBox<MapHandler.MapType> mapTypeCombo = new JComboBox<>(MapHandler.MapType.values());
        mapTypeCombo.setPreferredSize(comboSize);
        mapTypeCombo.setMaximumSize(comboSize);
        mapTypeCombo.setFocusable(false);
        mapTypeCombo.addActionListener(e
                -> mapHandler.switchMapType((MapHandler.MapType) mapTypeCombo.getSelectedItem())
        );

        // Buttons with same size
        JButton addPointBtn = new JButton("Add Points");
        addPointBtn.setPreferredSize(buttonSize);
        addPointBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        addPointBtn.addActionListener(e -> {
            mapHandler.getWaypointHandler().toggleAddingPoints();
            addPointBtn.setText(addPointBtn.getText().equals("Add Points") ? "Stop" : "Add Points");
        });

        JButton clearPointsBtn = new JButton("Clear");
        clearPointsBtn.setPreferredSize(buttonSize);
        clearPointsBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        clearPointsBtn.addActionListener(e -> mapHandler.getWaypointHandler().clearWaypoints());

        // Add components to the panel
        panel.add(mapTypeCombo);
        panel.add(addPointBtn);
        panel.add(clearPointsBtn);

        return panel;
    }
}
