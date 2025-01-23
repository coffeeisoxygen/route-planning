package com.coffeecode.view.map.cache;

import java.io.File;

import org.jxmapviewer.cache.FileBasedLocalCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapFileCache {

    private static final File CACHE_DIR = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2cache");
    private final FileBasedLocalCache cache;

    public MapFileCache() {
        initializeCache();
        this.cache = new FileBasedLocalCache(CACHE_DIR, false);
    }

    private void initializeCache() {
        log.debug("Initializing cache directory at: {}", CACHE_DIR.getAbsolutePath());
        if (!CACHE_DIR.exists()) {
            CACHE_DIR.mkdirs();
        }
    }

    public FileBasedLocalCache getCache() {
        return cache;
    }
}
