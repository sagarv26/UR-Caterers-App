package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskCache.Factory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory.CacheDirectoryGetter;
import java.io.File;

public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {

    class C04301 implements CacheDirectoryGetter {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$diskCacheName;

        C04301(Context context, String str) {
            this.val$context = context;
            this.val$diskCacheName = str;
        }

        public File getCacheDirectory() {
            File cacheDirectory = this.val$context.getCacheDir();
            if (cacheDirectory == null) {
                return null;
            }
            if (this.val$diskCacheName != null) {
                return new File(cacheDirectory, this.val$diskCacheName);
            }
            return cacheDirectory;
        }
    }

    public InternalCacheDiskCacheFactory(Context context) {
        this(context, Factory.DEFAULT_DISK_CACHE_DIR, Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public InternalCacheDiskCacheFactory(Context context, int diskCacheSize) {
        this(context, Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public InternalCacheDiskCacheFactory(Context context, String diskCacheName, int diskCacheSize) {
        super(new C04301(context, diskCacheName), diskCacheSize);
    }
}
