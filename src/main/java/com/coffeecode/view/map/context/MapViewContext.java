package com.coffeecode.view.map.context;

import com.coffeecode.view.map.cache.MapFileCache;
import com.coffeecode.view.map.handler.MapHandler;
import com.coffeecode.view.map.handler.WaypointHandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class MapViewContext {
    private static MapViewContext instance;
    private final MapFileCache mapFileCache;
    private final MapHandler mapHandler;
    private final WaypointHandler waypointHandler;

    private MapViewContext() {
        this.mapFileCache = new MapFileCache();
        this.mapHandler = new MapHandler(mapFileCache);
        this.waypointHandler = new WaypointHandler(mapHandler.getMapViewer());
    }

    public static synchronized MapViewContext getInstance() {
        if (instance == null) {
            instance = new MapViewContext();
        }
        return instance;
    }
}
