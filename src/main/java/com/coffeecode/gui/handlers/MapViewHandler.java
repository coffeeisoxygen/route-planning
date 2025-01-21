package com.coffeecode.gui.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.MapViewController;
import com.coffeecode.gui.models.MapViewModel;

@Component
public class MapViewHandler {

    private final MapViewController controller;
    private final MapViewModel model;

    public MapViewHandler(MapViewController controller, MapViewModel model) {
        this.controller = controller;
        this.model = model;
    }

    public MouseAdapter createMouseAdapter(JXMapViewer mapViewer) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    handleDoubleClick(e, mapViewer);
                }
            }
        };
    }

    private void handleDoubleClick(MouseEvent e, JXMapViewer mapViewer) {
        GeoPosition pos = mapViewer.convertPointToGeoPosition(e.getPoint());
        String name = JOptionPane.showInputDialog("Location name:");
        if (name != null && !name.trim().isEmpty()) {
            controller.addLocation(name, pos.getLatitude(), pos.getLongitude());
        }
    }

    public void zoomIn(JXMapViewer mapViewer) {
        model.setZoom(model.getZoom() - 1);
        mapViewer.setZoom(model.getZoom());
    }

    public void zoomOut(JXMapViewer mapViewer) {
        model.setZoom(model.getZoom() + 1);
        mapViewer.setZoom(model.getZoom());
    }
}
