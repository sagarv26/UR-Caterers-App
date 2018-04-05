package com.bumptech.glide.load.engine;

public enum DiskCacheStrategy {
    ALL(true, true),
    NONE(false, false),
    SOURCE(true, false),
    RESULT(false, true);
    
    private final boolean cacheResult;
    private final boolean cacheSource;

    private DiskCacheStrategy(boolean cacheSource, boolean cacheResult) {
        this.cacheSource = cacheSource;
        this.cacheResult = cacheResult;
    }

    public boolean cacheSource() {
        return this.cacheSource;
    }

    public boolean cacheResult() {
        return this.cacheResult;
    }
}
