package com.coffeecode.gui.panels;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import org.jxmapviewer.*;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.painter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// Add these imports
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.gui.controllers.MapViewController;
import com.coffeecode.gui.event.LocationUpdateListener;
import com.coffeecode.gui.handlers.MapViewHandler;
import com.coffeecode.gui.models.MapViewModel;
import com.coffeecode.gui.painters.*;

@Component
public class MapView extends JPanel implements LocationUpdateListener {

    private final JXMapViewer mapViewer;
    private final MapViewController controller;
    private final MapViewModel model;
    private final MapViewHandler handler;
    private final WaypointPainter<LocationWaypoint> waypointPainter;
    private final RoutePainter routePainter;
    private final Set<LocationWaypoint> waypoints;

    @Autowired
    public MapView(MapViewController controller, MapViewModel model, MapViewHandler handler) {
        this.controller = controller;
        this.model = model;
        this.handler = handler;
        this.waypoints = new HashSet<>();

        setLayout(new BorderLayout());

        // Initialize map
        mapViewer = new JXMapViewer();
        setupMapViewer();

        // Setup painters
        waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(new LocationWaypointRenderer());
        routePainter = new RoutePainter();

        // Combine painters
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>();
        painter.addPainter(routePainter);
        painter.addPainter(waypointPainter);
        mapViewer.setOverlayPainter(painter);

        // Add components
        add(createControlPanel(), BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);

        // Register for updates
        controller.addListener(this);
    }

    private void setupMapViewer() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Initial view (Bandung)
        mapViewer.setZoom(8);
        mapViewer.setAddressLocation(new GeoPosition(-6.914744, 107.609810));

        setupMouseHandlers();
    }

    private void setupMouseHandlers() {
        MouseAdapter clickHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                GeoPosition clickPos = mapViewer.convertPointToGeoPosition(e.getPoint());

                if (e.getClickCount() == 1) {
                    // Single click - select location
                    handleSingleClick(clickPos);
                } else if (e.getClickCount() == 2) {
                    // Double click - add new location
                    handleDoubleClick(clickPos);
                }
            }
        };

        // Add mouse listeners
        MouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseListener(clickHandler);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
    }

    private void handleSingleClick(GeoPosition pos) {
        // Find if we clicked near an existing waypoint
        Optional<DefaultWaypoint> nearestWaypoint = findNearestWaypoint(pos);
        if (nearestWaypoint.isPresent()) {
            controller.selectLocation(pos.getLatitude(), pos.getLongitude());
        }
    }

    private void handleDoubleClick(GeoPosition pos) {
        // Show dialog to add new location
        String name = JOptionPane.showInputDialog(this,
                "Enter location name:",
                "Add New Location",
                JOptionPane.PLAIN_MESSAGE);

        if (name != null && !name.trim().isEmpty()) {
            controller.addNewLocation(name, pos.getLatitude(), pos.getLongitude());
        }
    }

    private Optional<DefaultWaypoint> findNearestWaypoint(GeoPosition clickPos) {
        final double CLICK_THRESHOLD = 0.0001; // about 11 meters

        return waypoints.stream()
                .min((w1, w2) -> {
                    double d1 = calculateDistance(clickPos, w1.getPosition());
                    double d2 = calculateDistance(clickPos, w2.getPosition());
                    return Double.compare(d1, d2);
                })
                .filter(w -> calculateDistance(clickPos, w.getPosition()) < CLICK_THRESHOLD);
    }

    private double calculateDistance(GeoPosition p1, GeoPosition p2) {
        return Math.sqrt(
                Math.pow(p1.getLatitude() - p2.getLatitude(), 2)
                + Math.pow(p1.getLongitude() - p2.getLongitude(), 2)
        );
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addwaypoint = new JButton("Add Waypoint");
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        JButton reset = new JButton("Reset View");

        zoomIn.addActionListener(e -> handler.zoomIn(mapViewer));
        zoomOut.addActionListener(e -> handler.zoomOut(mapViewer));
        reset.addActionListener(e -> controller.zoomToFitAll());

        panel.add(zoomIn);
        panel.add(zoomOut);
        panel.add(reset);

        return panel;
    }

    @Override
    public void onLocationsUpdated(java.util.List<Locations> locations) {
        waypoints.clear();
        locations.forEach(loc
                -> waypoints.add(new LocationWaypoint(loc)));
        waypointPainter.setWaypoints(waypoints);
        mapViewer.repaint();
    }

    @Override
    public void onLocationSelected(Locations location) {
        if (location != null) {
            GeoPosition pos = new GeoPosition(location.latitude(), location.longitude());
            mapViewer.setAddressLocation(pos);
            mapViewer.setZoom(4);
            mapViewer.repaint();

            // Update waypoint states
            waypoints.forEach(wp
                    -> wp.setSelected(wp.getLocation().equals(location)));
        }
    }

    @Override
    public void onPathCalculated(Locations start, Locations end, java.util.List<Locations> path) {
        java.util.List<GeoPosition> track = path.stream()
                .map(loc -> new GeoPosition(loc.latitude(), loc.longitude()))
                .toList();
        routePainter.setTrack(track);
        mapViewer.repaint();
    }
}
