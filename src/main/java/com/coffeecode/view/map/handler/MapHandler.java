package com.coffeecode.view.map.handler;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.coffeecode.view.map.cache.MapFileCache;

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

    @Getter
    private final Map<MapType, DefaultTileFactory> tileFactoryCache = new EnumMap<>(MapType.class);
    @Getter
    private final JXMapViewer mapViewer;
    @Getter
    private final WaypointHandler waypointHandler;
    private final MapFileCache mapCache;

    public MapHandler(MapFileCache mapCache) {
        this.mapCache = mapCache;
        this.mapViewer = initializeMapViewer();
        this.waypointHandler = new WaypointHandler(mapViewer);
    }

    private JXMapViewer initializeMapViewer() {
        log.debug("Initializing map viewer");
        JXMapViewer viewer = new JXMapViewer();
        viewer.setTileFactory(createTileFactory(new OSMTileFactoryInfo()));
        viewer.setZoom(8);
        viewer.setAddressLocation(new GeoPosition(-6.200000, 106.816666));
        addDefaultInteractions(viewer);
        return viewer;
    }

    private DefaultTileFactory createTileFactory(TileFactoryInfo info) {
        log.debug("Creating tile factory for: {}", info.getName());
        DefaultTileFactory factory = new DefaultTileFactory(info);
        factory.setLocalCache(mapCache.getCache());
        factory.setThreadPoolSize(8);
        return factory;
    }

    private void addDefaultInteractions(JXMapViewer viewer) {
        log.debug("Adding default interactions to map viewer");
        MouseInputListener mouseListener = new PanMouseInputListener(viewer);
        viewer.addMouseListener(mouseListener);
        viewer.addMouseMotionListener(mouseListener);
        viewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(viewer));
    }

    public void switchMapType(MapType mapType) {
        log.info("Switching map type to: {}", mapType);
        int zoom = mapViewer.getZoom();
        GeoPosition center = mapViewer.getCenterPosition();

        DefaultTileFactory factory = tileFactoryCache.computeIfAbsent(mapType, this::createFactoryForType);
        mapViewer.setTileFactory(factory);

        mapViewer.setZoom(zoom);
        mapViewer.setAddressLocation(center);
        mapViewer.repaint();
    }

    private DefaultTileFactory createFactoryForType(MapType mapType) {
        log.debug("Creating factory for map type: {}", mapType);
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
