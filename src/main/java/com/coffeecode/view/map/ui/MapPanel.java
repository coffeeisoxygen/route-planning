package com.coffeecode.view.map.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.coffeecode.view.map.context.MapViewContext;
import com.coffeecode.view.map.handler.MapHandler;
import com.coffeecode.view.map.handler.WaypointHandler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final transient MapHandler mapHandler;
    private final transient WaypointHandler waypointHandler;

    @Builder
    public MapPanel(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
        this.waypointHandler = MapViewContext.getInstance().getWaypointHandler();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(mapHandler.getMapViewer(), BorderLayout.CENTER);

        // Add overlay control panel
        JPanel controlPanel = createControlPanel();
        mapHandler.getMapViewer().setLayout(null);
        mapHandler.getMapViewer().add(controlPanel);
        // Increase width to accommodate all buttons
        controlPanel.setBounds(10, 10, 400, 35);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make panel visible
        panel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        panel.setPreferredSize(new Dimension(400, 35));  // Match bounds width

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
