package com.coffeecode.view.map.handler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import lombok.Getter;

public class WaypointHandler {

    private final JXMapViewer mapViewer;
    @Getter
    private final Set<DefaultWaypoint> waypoints;
    private final WaypointPainter<DefaultWaypoint> waypointPainter;
    private boolean addingEnabled = false;

    public WaypointHandler(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        this.waypoints = new HashSet<>();
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
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    public void addWaypoint(GeoPosition pos) {
        waypoints.add(new DefaultWaypoint(pos));
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
