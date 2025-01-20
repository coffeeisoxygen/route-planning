package com.coffeecode.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import com.coffeecode.gui.controllers.LocationController;

public class MapDialog extends JDialog {
    private final JXMapViewer mapViewer;
    private final LocationController controller;
    private GeoPosition selectedPosition;

    public MapDialog(JFrame parent, LocationController controller) {
        super(parent, "Select Location", true);
        this.controller = controller;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        
        // Initialize map
        mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Set default position (e.g., Jakarta)
        GeoPosition jakarta = new GeoPosition(-6.200000, 106.816666);
        mapViewer.setAddressLocation(jakarta);
        mapViewer.setZoom(7);

        // Add click listener
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                showNameDialog();
            }
        });

        // Layout
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
    }

    private void showNameDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter location name:");
        if (name != null && !name.trim().isEmpty()) {
            controller.addLocation(name, 
                selectedPosition.getLatitude(), 
                selectedPosition.getLongitude());
            dispose();
        }
    }
}
