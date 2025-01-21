package com.coffeecode.presentation.view.components.map;

import javax.swing.*;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import com.coffeecode.presentation.viewmodel.LocationViewModel;

import java.awt.Component;

public class MapContextMenu extends JPopupMenu {

    private final JXMapViewer mapViewer;
    private final LocationViewModel viewModel;
    private GeoPosition selectedPosition;

    public MapContextMenu(JXMapViewer mapViewer, LocationViewModel viewModel) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;

        // Add menu items
        JMenuItem centerItem = new JMenuItem("Center Map Here");
        centerItem.addActionListener(e -> centerMap());
        
        JMenuItem addItem = new JMenuItem("Add Location");
        addItem.addActionListener(e -> addLocation());

        add(centerItem);
        add(addItem);
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

    private void addLocation() {
        if (selectedPosition != null) {
            String name = JOptionPane.showInputDialog(this,
                "Enter location name:",
                "Add New Location",
                JOptionPane.PLAIN_MESSAGE);
                
            if (name != null && !name.trim().isEmpty()) {
                viewModel.addLocation(name, 
                    selectedPosition.getLatitude(), 
                    selectedPosition.getLongitude());
            }
        }
    }

    public void show(Component invoker, int x, int y, GeoPosition position) {
        this.selectedPosition = position;
        super.show(invoker, x, y);
    }
}
