package com.coffeecode.view.map.cache;

import java.io.File;

import org.jxmapviewer.cache.FileBasedLocalCache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class MapFileCache {

    private static final File CACHE_DIR = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2cache");
    private final FileBasedLocalCache cache;

    public MapFileCache() {
        if (!CACHE_DIR.exists()) {
            log.debug("Creating cache directory at: {}", CACHE_DIR.getAbsolutePath());
            CACHE_DIR.mkdirs();
        }
        this.cache = new FileBasedLocalCache(CACHE_DIR, false);
    }

    public FileBasedLocalCache getCache() {
        return cache;
    }
}
