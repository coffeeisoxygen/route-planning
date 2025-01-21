package com.coffeecode.presentation.view.components.map;

import javax.swing.*;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import java.awt.Component;

public class MapContextMenu extends JPopupMenu {

    private final JXMapViewer mapViewer;
    private GeoPosition selectedPosition;

    public MapContextMenu(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;

        // Add menu items
        JMenuItem centerItem = new JMenuItem("Center Map Here");
        centerItem.addActionListener(e -> centerMap());
        add(centerItem);
    }

    private void centerMap() {
        if (selectedPosition != null) {
            // Center the map on the selected position
            mapViewer.setCenterPosition(selectedPosition);
            // Optional: Set zoom level
            mapViewer.setZoom(3);
            // Repaint to reflect changes
            mapViewer.repaint();
        }
    }

    public void show(Component invoker, int x, int y, GeoPosition position) {
        this.selectedPosition = position;
        super.show(invoker, x, y);
    }
}
