package com.coffeecode.presentation.view.components.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import com.coffeecode.presentation.viewmodel.impl.LocationViewModel;

public class LocationWaypointRenderer implements WaypointRenderer<LocationWaypoint> {

    private final LocationViewModel viewModel;
    private ImageIcon defaultIcon;
    private ImageIcon selectedIcon;
    private final Color defaultColor = new Color(0, 120, 255);
    private final Color selectedColor = new Color(255, 0, 0);

    public LocationWaypointRenderer(LocationViewModel viewModel) {
        this.viewModel = viewModel;
        tryLoadIcons();
    }

    private void tryLoadIcons() {
        try {
            ClassLoader cl = getClass().getClassLoader();
            defaultIcon = new ImageIcon(cl.getResource("icons/marker.png"));
            selectedIcon = new ImageIcon(cl.getResource("icons/marker-selected.png"));
        } catch (Exception e) {
            defaultIcon = null;
            selectedIcon = null;
        }
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, LocationWaypoint waypoint) {
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
        boolean isSelected = waypoint.getLocation().equals(viewModel.getSelectedLocation());

        if (defaultIcon != null && selectedIcon != null) {
            paintIcon(g, point, isSelected);
        } else {
            paintCircle(g, point, isSelected);
        }
    }

    private void paintIcon(Graphics2D g, Point2D point, boolean isSelected) {
        ImageIcon icon = isSelected ? selectedIcon : defaultIcon;
        g.drawImage(icon.getImage(),
                (int) point.getX() - icon.getIconWidth() / 2,
                (int) point.getY() - icon.getIconHeight(),
                null);
    }

    private void paintCircle(Graphics2D g, Point2D point, boolean isSelected) {
        int size = 10;
        g.setColor(isSelected ? selectedColor : defaultColor);
        g.fillOval((int) point.getX() - size / 2,
                (int) point.getY() - size / 2,
                size, size);
    }
}
