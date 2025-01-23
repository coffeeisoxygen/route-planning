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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaypointHandler {

    @Getter
    private final JXMapViewer mapViewer;
    @Getter
    private final Set<DefaultWaypoint> waypoints = new HashSet<>();
    @Getter
    private final WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
    @Getter
    @Setter
    private boolean addingEnabled;

    public WaypointHandler(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
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
        log.debug("Adding waypoint at: lat={}, lon={}", pos.getLatitude(), pos.getLongitude());
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
