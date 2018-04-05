package com.bumptech.glide.load.engine.cache;

import android.annotation.SuppressLint;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache.ResourceRemovedListener;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {
    private ResourceRemovedListener listener;

    public /* bridge */ /* synthetic */ Resource put(Key x0, Resource x1) {
        return (Resource) super.put(x0, x1);
    }

    public /* bridge */ /* synthetic */ Resource remove(Key x0) {
        return (Resource) super.remove(x0);
    }

    public LruResourceCache(int size) {
        super(size);
    }

    public void setResourceRemovedListener(ResourceRemovedListener listener) {
        this.listener = listener;
    }

    protected void onItemEvicted(Key key, Resource<?> item) {
        if (this.listener != null) {
            this.listener.onResourceRemoved(item);
        }
    }

    protected int getSize(Resource<?> item) {
        return item.getSize();
    }

    @SuppressLint({"InlinedApi"})
    public void trimMemory(int level) {
        if (level >= 60) {
            clearMemory();
        } else if (level >= 40) {
            trimToSize(getCurrentSize() / 2);
        }
    }
}
