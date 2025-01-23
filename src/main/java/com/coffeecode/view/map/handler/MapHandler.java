package com.coffeecode.view.map.handler;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
public class MapHandler {

    // Inner enum definition
    public enum MapType {
        OPENSTREETMAP("OpenStreetMap"),
        SATELLITE("Satellite"),
        VIRTUALEARTH("Virtual Earth"),
        HYBRID("Hybrid");

        private final String displayName;

        MapType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private static final File CACHE_DIR = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2cache");
    private final Map<MapType, DefaultTileFactory> tileFactoryCache = new EnumMap<>(MapType.class);
    private final JXMapViewer mapViewer = initializeMapViewer();
    private final WaypointHandler waypointHandler = new WaypointHandler(mapViewer);

    private void initializeCache() {
        if (!CACHE_DIR.exists()) {
            CACHE_DIR.mkdirs();
        }
    }

    private JXMapViewer initializeMapViewer() {
        JXMapViewer viewer = new JXMapViewer();

        // Set default OSM tile factory with cache
        viewer.setTileFactory(createTileFactory(new OSMTileFactoryInfo()));

        // Set default zoom and location
        viewer.setZoom(8);
        viewer.setAddressLocation(new GeoPosition(-6.200000, 106.816666));

        // Add interaction handlers
        addDefaultInteractions(viewer);

        return viewer;
    }

    private DefaultTileFactory createTileFactory(TileFactoryInfo info) {
        DefaultTileFactory factory = new DefaultTileFactory(info);

        // Configure cache
        FileBasedLocalCache cache = new FileBasedLocalCache(CACHE_DIR, false);
        factory.setLocalCache(cache);

        // Optimize loading
        factory.setThreadPoolSize(8);
        return factory;
    }

    private void addDefaultInteractions(JXMapViewer viewer) {
        MouseInputListener mouseListener = new PanMouseInputListener(viewer);
        viewer.addMouseListener(mouseListener);
        viewer.addMouseMotionListener(mouseListener);
        viewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(viewer));
    }

    public void switchMapType(MapType mapType) {
        log.info("Switching map type to: {}", mapType);
        // Store current view state
        int zoom = mapViewer.getZoom();
        GeoPosition center = mapViewer.getCenterPosition();

        // Switch tile factory
        DefaultTileFactory factory = tileFactoryCache.computeIfAbsent(mapType, this::createFactoryForType);
        mapViewer.setTileFactory(factory);

        // Restore view state
        mapViewer.setZoom(zoom);
        mapViewer.setAddressLocation(center);
        mapViewer.repaint();
    }

    private DefaultTileFactory createFactoryForType(MapType mapType) {
        TileFactoryInfo info;
        switch (mapType) {
            case SATELLITE:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
                break;
            case VIRTUALEARTH:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
                break;
            case HYBRID:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
                break;
            case OPENSTREETMAP:
            default:
                info = new OSMTileFactoryInfo();
                break;
        }
        return createTileFactory(info);
    }
}
