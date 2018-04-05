package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache.ResourceRemovedListener;

public class MemoryCacheAdapter implements MemoryCache {
    private ResourceRemovedListener listener;

    public int getCurrentSize() {
        return 0;
    }

    public int getMaxSize() {
        return 0;
    }

    public void setSizeMultiplier(float multiplier) {
    }

    public Resource<?> remove(Key key) {
        return null;
    }

    public Resource<?> put(Key key, Resource<?> resource) {
        this.listener.onResourceRemoved(resource);
        return null;
    }

    public void setResourceRemovedListener(ResourceRemovedListener listener) {
        this.listener = listener;
    }

    public void clearMemory() {
    }

    public void trimMemory(int level) {
    }
}
