package com.coffeecode.view.map.handler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

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

    @Getter
    private final JXMapViewer mapViewer;
    @Getter
    private final Set<DefaultWaypoint> waypoints = new HashSet<>();
    @Getter
    private final WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
    @Getter
    @Setter
    private boolean addingEnabled;

    private BufferedImage waypointIcon; // Lazy-loaded icon

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
        waypointPainter.setRenderer((g, map, wp) -> {
            if (waypointIcon == null) {
                waypointIcon = loadWaypointIcon();
            }

            if (waypointIcon != null) {
                Point2D point = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
                g.drawImage(waypointIcon,
                        (int) point.getX() - waypointIcon.getWidth() / 2,
                        (int) point.getY() - waypointIcon.getHeight(),
                        null);
            }
        });
        mapViewer.setOverlayPainter(waypointPainter);
    }

    private BufferedImage loadWaypointIcon() {
        try {
            // Try multiple fallback paths
            BufferedImage icon = ImageIO.read(getClass().getResourceAsStream("/icons/waypoint-normal.png"));
            if (icon == null) {
                icon = ImageIO.read(getClass().getResourceAsStream("src\\main\\resources\\icons\\waypoint-highlight.png"));
            }
            if (icon == null) {
                // Create a simple circle as fallback
                icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = icon.createGraphics();
                g.setColor(Color.RED);
                g.fillOval(0, 0, 15, 15);
                g.dispose();
            }
            return icon;
        } catch (IOException e) {
            log.error("Error loading waypoint icon", e);
            return null;
        }
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
