package com.coffeecode.view.map.handler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class WaypointHandler {

    private static final int MARKER_SIZE = 100;
    private final JXMapViewer mapViewer;
    private final Set<DefaultWaypoint> waypoints = new HashSet<>();
    private final WaypointPainter<DefaultWaypoint> waypointPainter;
    @Setter
    private boolean addingEnabled;

    public WaypointHandler(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        this.waypointPainter = new WaypointPainter<>();
        setupMouseListener();
        setupPainter();
    }

    private void setupMouseListener() {
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (addingEnabled && e.getButton() == MouseEvent.BUTTON1) {
                    GeoPosition clickPos = mapViewer.convertPointToGeoPosition(e.getPoint());
                    addWaypoint(clickPos);
                }
            }
        });
    }

    private void setupPainter() {
        waypointPainter.setRenderer((Graphics2D g, JXMapViewer map, DefaultWaypoint wp) -> {
            Point2D point = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());

            // Center the marker
            int x = (int) point.getX() - MARKER_SIZE / 2;
            int y = (int) point.getY() - MARKER_SIZE / 2;

            // Draw marker with better visibility
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Outer circle (border)
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3));
            g.drawOval(x, y, MARKER_SIZE, MARKER_SIZE);

            // Inner circle (fill)
            g.setColor(new Color(255, 0, 0, 200)); // Semi-transparent red
            g.fillOval(x, y, MARKER_SIZE, MARKER_SIZE);
        });

        mapViewer.setOverlayPainter(waypointPainter);
    }

    public void addWaypoint(GeoPosition pos) {
        DefaultWaypoint waypoint = new DefaultWaypoint(pos);
        waypoints.add(waypoint);
        log.debug("Added waypoint at: lat={}, lon={}", pos.getLatitude(), pos.getLongitude());
        updatePainter();
    }

    public void clearWaypoints() {
        waypoints.clear();
        updatePainter();
    }

    private void updatePainter() {
        waypointPainter.setWaypoints(waypoints);
        mapViewer.repaint();
    }

    public void toggleAddingPoints() {
        addingEnabled = !addingEnabled;
    }
}
