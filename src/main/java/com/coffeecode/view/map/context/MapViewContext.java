package com.coffeecode.view.map.context;

import com.coffeecode.view.map.cache.MapFileCache;
import com.coffeecode.view.map.handler.MapHandler;
import com.coffeecode.view.map.handler.WaypointHandler;

import lombok.Getter;

@Getter
public class MapViewContext {

    @Getter(lazy = true)
    private static final MapViewContext instance = new MapViewContext();

    private final MapFileCache mapFileCache;
    private final MapHandler mapHandler;
    private final WaypointHandler waypointHandler;

    private MapViewContext() {
        this.mapFileCache = new MapFileCache();
        this.mapHandler = new MapHandler(mapFileCache);
        this.waypointHandler = new WaypointHandler(mapHandler.getMapViewer());
    }

    // No need for getInstance() method as we use @Getter(lazy=true)
}
