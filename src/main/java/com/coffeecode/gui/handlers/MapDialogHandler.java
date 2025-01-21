package com.coffeecode.gui.handlers;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.models.MapDialogModel;
import com.coffeecode.gui.models.MapDialogModel.SaveStatus;
import com.coffeecode.gui.models.MapDialogModel.TempLocation;

@Component
public class MapDialogHandler {

    private final JXMapViewer mapViewer;
    private final LocationController controller;
    private final MapDialogModel model;

    @Autowired
    public MapDialogHandler(LocationController controller, MapDialogModel model) {
        this.controller = controller;
        this.model = model;
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
        mapViewer.setTileFactory(new DefaultTileFactory(info));
    }

    public void addLocation(String name, GeoPosition position) {
        TempLocation temp = new TempLocation(name, position, SaveStatus.UNSAVED);
        model.getTempLocations().add(temp);
        model.getListModel().addElement(temp);
        updateWaypoints();
    }

    public void updateWaypoints() {
        model.getWaypoints().clear();
        model.getTempLocations().forEach(loc
                -> model.getWaypoints().add(new DefaultWaypoint(loc.position())));

        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(model.getWaypoints());
        mapViewer.setOverlayPainter(painter);
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

    public void saveLocations() {
        for (TempLocation temp : model.getTempLocations()) {
            controller
                    .addLocation(temp.name(), temp.position()
                            .getLatitude(), temp.position().getLongitude());
        }
        model.getTempLocations().clear();
        model.getListModel().clear();
        model.getWaypoints().clear();
        mapViewer.setOverlayPainter(null);
    }
}
