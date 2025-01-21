package com.coffeecode.gui.handlers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.models.MapDialogModel;

@Component
public class MapDialogHandler {

    private final JXMapViewer mapViewer;
    private final LocationController controller;

    @Autowired
    public MapDialogHandler(LocationController controller) {
        this.controller = controller;
        this.mapViewer = new JXMapViewer();
        initializeMap();
    }

    private void initializeMap() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        mapViewer.setTileFactory(new DefaultTileFactory(info));
        mapViewer.setAddressLocation(MapDialogModel.DEFAULT_LOCATIONS.get("Jakarta"));
        mapViewer.setZoom(7);
    }

    public void switchMapType(int type) {
        TileFactoryInfo info = switch (type) {
            case 0 ->
                new OSMTileFactoryInfo();
            case 1 ->
                new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
            default ->
                new OSMTileFactoryInfo();
        };
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Store current view state
        GeoPosition currentPosition = mapViewer.getCenterPosition();
        int currentZoom = mapViewer.getZoom();

        mapViewer.setTileFactory(tileFactory);

        // Restore view state
        mapViewer.setAddressLocation(currentPosition);
        mapViewer.setZoom(currentZoom);
    }

    public void addLocationToMap(String name, GeoPosition position) {
        DefaultWaypoint waypoint = new DefaultWaypoint(position) {
            @Override
            public String toString() {
                return name;
            }
        };
        updateWaypoint(waypoint);
    }

    public void updateWaypoint(Waypoint waypoint) {
        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        Set<Waypoint> waypoints = Set.of(waypoint);
        painter.setWaypoints(waypoints);
        painter.setRenderer(createWaypointRenderer());
        mapViewer.setOverlayPainter(painter);
    }

    private WaypointRenderer<Waypoint> createWaypointRenderer() {
        return (Graphics2D g, JXMapViewer map, Waypoint wp) -> {
            Point2D point = map.getTileFactory().geoToPixel(
                    wp.getPosition(), map.getZoom());

            g.setColor(Color.RED);
            g.fillOval((int) point.getX() - 5, (int) point.getY() - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(wp.toString(), (int) point.getX() + 5, (int) point.getY());
        };
    }

    public void goToLocation(String locationName) {
        GeoPosition position = MapDialogModel.DEFAULT_LOCATIONS.get(locationName);
        if (position != null) {
            mapViewer.setAddressLocation(position);
        }
    }

    public void zoom(boolean in) {
        int zoom = mapViewer.getZoom();
        mapViewer.setZoom(in ? zoom - 1 : zoom + 1);
    }

    public void saveLocation(String name, GeoPosition position) {
        controller.addLocation(name,
                position.getLatitude(),
                position.getLongitude());
    }

    public JXMapViewer getMapViewer() {
        return mapViewer;
    }
}
