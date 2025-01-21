package com.coffeecode.presentation.view.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import org.springframework.stereotype.Component;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.viewmodel.LocationViewModel;
import com.coffeecode.presentation.viewmodel.LocationViewModel.ViewModelListener;
import com.coffeecode.presentation.viewmodel.LocationWaypoint;

@Component
public class MapComponent extends JPanel implements ViewModelListener {

    private final JXMapViewer mapViewer;
    private final LocationViewModel viewModel;
    private final WaypointPainter<LocationWaypoint> waypointPainter;

    public MapComponent(JXMapViewer mapViewer, LocationViewModel viewModel) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;
        this.waypointPainter = new WaypointPainter<>();

        setupMapViewer();
        initializeMap();

        viewModel.addListener(this);
        viewModel.loadLocations();
    }

    private void setupMapViewer() {
        setLayout(new BorderLayout());

        // Set up tile factory
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Add to panel
        add(mapViewer, BorderLayout.CENTER);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    private void initializeMap() {
        // Set initial position (Bandung)
        GeoPosition bandung = new GeoPosition(-6.914744, 107.609810);
        mapViewer.setAddressLocation(bandung);

        // Set zoom level (smaller = more zoomed in)
        mapViewer.setZoom(8);

        // Optional: Set minimum/maximum zoom
        mapViewer.setMinimumSize(new Dimension(100, 100));
        mapViewer.setMaximumSize(new Dimension(2000, 2000));
    }

    @Override
    public void onLocationsChanged(List<Locations> locations) {
        // Will implement later
    }

    @Override
    public void onLocationSelected(Locations location) {
        // Will implement later
    }
}
