package com.coffeecode.presentation.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import org.springframework.stereotype.Component;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.view.components.map.LocationInfoPanel;
import com.coffeecode.presentation.view.components.map.LocationWaypoint;
import com.coffeecode.presentation.view.components.map.LocationWaypointRenderer;
import com.coffeecode.presentation.view.components.map.MapContextMenu;
import com.coffeecode.presentation.view.components.map.MapMouseHandler;
import com.coffeecode.presentation.viewmodel.base.Observer;
import com.coffeecode.presentation.viewmodel.impl.LocationViewModel;

@Component
public class MapComponent extends JPanel implements Observer {

    private final JXMapViewer mapViewer;
    private final LocationViewModel viewModel;
    private final WaypointPainter<LocationWaypoint> waypointPainter;
    private final MapContextMenu contextMenu;
    private final Set<LocationWaypoint> waypoints;

    public MapComponent(JXMapViewer mapViewer, LocationViewModel viewModel) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;
        this.waypointPainter = new WaypointPainter<>();
        this.contextMenu = new MapContextMenu(mapViewer, viewModel);
        this.waypoints = new HashSet<>();

        setupMapViewer();
        setupMouseHandlers();
        initializeMap();

        viewModel.addObserver(this);
        viewModel.loadLocations();
    }

    private void setupMapViewer() {
        setLayout(new BorderLayout());

        // Set up tile factory
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Set waypoint renderer
        waypointPainter.setRenderer(new LocationWaypointRenderer(viewModel));

        // Add to panel
        add(mapViewer, BorderLayout.CENTER);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    private void setupMouseHandlers() {
        MapMouseHandler mouseHandler = new MapMouseHandler(mapViewer, contextMenu,
                new LocationInfoPanel(null)) {
            @Override
            public void mouseClicked(MouseEvent e) {
                GeoPosition pos = mapViewer.convertPointToGeoPosition(e.getPoint());
                if (SwingUtilities.isRightMouseButton(e)) {
                    displayContextMenu(e);
                } else {
                    findNearestWaypoint(pos).ifPresent(wp
                            -> viewModel.selectLocation(wp.getLocation()));
                }
            }

            protected void displayContextMenu(MouseEvent e) {
                GeoPosition pos = mapViewer.convertPointToGeoPosition(e.getPoint());
                contextMenu.show(mapViewer, e.getX(), e.getY(), pos);
            }
        };

        mapViewer.addMouseListener(mouseHandler);
        mapViewer.addMouseMotionListener(mouseHandler);
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
        waypoints.clear();
        if (locations != null) {
            for (Locations loc : locations) {
                waypoints.add(new LocationWaypoint(loc));
            }
        }
        waypointPainter.setWaypoints(waypoints);
        mapViewer.repaint();
    }

    @Override
    public void onLocationSelected(Locations location) {
        // Center map on selected location
        GeoPosition pos = new GeoPosition(location.latitude(), location.longitude());
        mapViewer.setCenterPosition(pos);
        mapViewer.repaint();
    }

    private Optional<LocationWaypoint> findNearestWaypoint(GeoPosition pos) {
        final double CLICK_THRESHOLD = 0.001; // About 111 meters
        return waypoints.stream()
                .min((w1, w2) -> Double.compare(
                calculateDistance(pos, w1.getPosition()),
                calculateDistance(pos, w2.getPosition())
        ))
                .filter(w -> calculateDistance(pos, w.getPosition()) < CLICK_THRESHOLD);
    }

    private double calculateDistance(GeoPosition p1, GeoPosition p2) {
        return Math.sqrt(
                Math.pow(p1.getLatitude() - p2.getLatitude(), 2)
                + Math.pow(p1.getLongitude() - p2.getLongitude(), 2)
        );
    }
}
