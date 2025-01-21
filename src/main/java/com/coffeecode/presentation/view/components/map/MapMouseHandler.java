package com.coffeecode.presentation.view.components.map;

import java.awt.event.*;
import javax.swing.SwingUtilities;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

public class MapMouseHandler extends MouseAdapter {

    private final JXMapViewer mapViewer;
    private final MapContextMenu contextMenu;
    private final LocationInfoPanel infoPanel;

    public MapMouseHandler(JXMapViewer mapViewer, MapContextMenu contextMenu, LocationInfoPanel infoPanel) {
        this.mapViewer = mapViewer;
        this.contextMenu = contextMenu;
        this.infoPanel = infoPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            handleZoom(e);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            handleSelect(e);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            showContextMenu(e);
        }
    }

    private void handleSelect(MouseEvent e) {
        // Implementation for handleSelect
    }

    private void showContextMenu(MouseEvent e) {
        // Implementation for showContextMenu
    }

    private void handleZoom(MouseEvent e) {
        GeoPosition pos = mapViewer.convertPointToGeoPosition(e.getPoint());
        mapViewer.setAddressLocation(pos);
        mapViewer.setZoom(mapViewer.getZoom() - 1);
    }
}
