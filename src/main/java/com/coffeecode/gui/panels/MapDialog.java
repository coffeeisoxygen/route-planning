package com.coffeecode.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.painter.WaypointPainter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import com.coffeecode.gui.controllers.LocationController;

public class MapDialog extends JDialog {
    private final JXMapViewer mapViewer;
    private final LocationController controller;
    private final List<TempLocation> tempLocations = new ArrayList<>();
    private final Set<Waypoint> waypoints = new HashSet<>();
    private GeoPosition selectedPosition;

    private static record TempLocation(String name, GeoPosition position) {}

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

        // Add map type selector
        JComboBox<String> mapTypeCombo = new JComboBox<>(new String[]{"OpenStreetMap", "Satellite"});
        mapTypeCombo.addActionListener(e -> switchMapType(mapTypeCombo.getSelectedIndex()));

        // Add zoom controls
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        zoomIn.addActionListener(e -> mapViewer.setZoom(mapViewer.getZoom() - 1));
        zoomOut.addActionListener(e -> mapViewer.setZoom(mapViewer.getZoom() + 1));

        // Add location list
        DefaultListModel<String> locationListModel = new DefaultListModel<>();
        JList<String> locationList = new JList<>(locationListModel);
        JScrollPane listScroll = new JScrollPane(locationList);
        listScroll.setPreferredSize(new Dimension(200, 0));

        // Add control panel
        JPanel controls = new JPanel(new FlowLayout());
        controls.add(mapTypeCombo);
        controls.add(zoomIn);
        controls.add(zoomOut);
        JButton saveButton = new JButton("Save All");
        JButton cancelButton = new JButton("Cancel");
        controls.add(saveButton);
        controls.add(cancelButton);

        saveButton.addActionListener(e -> saveLocations());
        cancelButton.addActionListener(e -> dispose());

        // Layout
        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
        add(listScroll, BorderLayout.EAST);

        // Update click listener
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                addNewLocation();
            }
        });
    }

    private void addNewLocation() {
        String name = JOptionPane.showInputDialog(this, "Enter location name:");
        if (name != null && !name.trim().isEmpty()) {
            TempLocation temp = new TempLocation(name, selectedPosition);
            tempLocations.add(temp);
            updateWaypoints();
            updateLocationList();
        }
    }

    private void updateWaypoints() {
        waypoints.clear();
        tempLocations.forEach(loc -> 
            waypoints.add(new DefaultWaypoint(loc.position())));
        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }

    private void saveLocations() {
        tempLocations.forEach(loc -> 
            controller.addLocation(loc.name(), 
                loc.position().getLatitude(), 
                loc.position().getLongitude()));
        dispose();
    }

    private void switchMapType(int type) {
        TileFactoryInfo info = switch(type) {
            case 0 -> new OSMTileFactoryInfo();
            case 1 -> new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
            default -> new OSMTileFactoryInfo();
        };
        mapViewer.setTileFactory(new DefaultTileFactory(info));
    }
}
