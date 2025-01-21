package com.coffeecode.presentation.view.components.map;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import com.coffeecode.presentation.viewmodel.LocationViewModel;
import com.coffeecode.presentation.viewmodel.LocationWaypoint;

public class LocationWaypointRenderer implements WaypointRenderer<LocationWaypoint> {

    private final ImageIcon defaultIcon;
    private final ImageIcon selectedIcon;
    private final LocationViewModel viewModel;

    public LocationWaypointRenderer(LocationViewModel viewModel) {
        this.viewModel = viewModel;

        // Load using ClassLoader to find resources in jar
        ClassLoader cl = this.getClass().getClassLoader();
        this.defaultIcon = new ImageIcon(cl.getResource("icons/marker.png"));
        this.selectedIcon = new ImageIcon(cl.getResource("icons/marker-selected.png"));

        if (defaultIcon.getImage() == null || selectedIcon.getImage() == null) {
            throw new IllegalStateException("Could not load map icons");
        }

        scaleIcon(defaultIcon, 24, 24);
        scaleIcon(selectedIcon, 24, 24);
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, LocationWaypoint waypoint) {
        Point2D point = map.getTileFactory().geoToPixel(
                waypoint.getPosition(), map.getZoom());

        boolean isSelected = waypoint.getLocation().equals(
                viewModel.getSelectedLocation());

        ImageIcon icon = isSelected ? selectedIcon : defaultIcon;

        g.drawImage(icon.getImage(),
                (int) point.getX() - icon.getIconWidth() / 2,
                (int) point.getY() - icon.getIconHeight(),
                map);
    }

    private void scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon.setImage(scaled);
    }
}
