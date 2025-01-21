package com.coffeecode.gui.painters;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

public class LocationWaypointRenderer implements WaypointRenderer<LocationWaypoint> {

    private final BufferedImage normalIcon;
    private final BufferedImage selectedIcon;
    private final BufferedImage highlightIcon;

    public LocationWaypointRenderer() {
        try {
            normalIcon = ImageIO.read(getClass().getResourceAsStream("/icons/waypoint-normal.png"));
            selectedIcon = ImageIO.read(getClass().getResourceAsStream("/icons/waypoint-selected.png"));
            highlightIcon = ImageIO.read(getClass().getResourceAsStream("/icons/waypoint-highlight.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load waypoint icons", e);
        }
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, LocationWaypoint waypoint) {
        Point2D point = viewer.getTileFactory().geoToPixel(
                waypoint.getPosition(), viewer.getZoom());

        // Choose icon based on state
        BufferedImage icon = normalIcon;
        if (waypoint.isSelected()) {
            icon = selectedIcon;
        } else if (waypoint.isHighlighted()) {
            icon = highlightIcon;
        }

        // Draw icon centered on point
        g.drawImage(icon,
                (int) point.getX() - icon.getWidth() / 2,
                (int) point.getY() - icon.getHeight() / 2,
                null);

        // Draw name if zoomed in
        if (viewer.getZoom() <= 3) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(waypoint.getLocation().name(),
                    (int) point.getX() + icon.getWidth() / 2 + 5,
                    (int) point.getY());
        }
    }
}
