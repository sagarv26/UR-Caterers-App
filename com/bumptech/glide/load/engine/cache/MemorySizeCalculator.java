package com.bumptech.glide.load.engine.cache;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

public class MemorySizeCalculator {
    static final int BITMAP_POOL_TARGET_SCREENS = 4;
    static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;
    static final float MAX_SIZE_MULTIPLIER = 0.4f;
    static final int MEMORY_CACHE_TARGET_SCREENS = 2;
    private static final String TAG = "MemorySizeCalculator";
    private final int bitmapPoolSize;
    private final Context context;
    private final int memoryCacheSize;

    interface ScreenDimensions {
        int getHeightPixels();

        int getWidthPixels();
    }

    private static class DisplayMetricsScreenDimensions implements ScreenDimensions {
        private final DisplayMetrics displayMetrics;

        public DisplayMetricsScreenDimensions(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }

        public int getWidthPixels() {
            return this.displayMetrics.widthPixels;
        }

        public int getHeightPixels() {
            return this.displayMetrics.heightPixels;
        }
    }

    public MemorySizeCalculator(Context context) {
        this(context, (ActivityManager) context.getSystemService("activity"), new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics()));
    }

    MemorySizeCalculator(Context context, ActivityManager activityManager, ScreenDimensions screenDimensions) {
        this.context = context;
        int maxSize = getMaxSize(activityManager);
        int screenSize = (screenDimensions.getWidthPixels() * screenDimensions.getHeightPixels()) * 4;
        int targetPoolSize = screenSize * 4;
        int targetMemoryCacheSize = screenSize * 2;
        if (targetMemoryCacheSize + targetPoolSize <= maxSize) {
            this.memoryCacheSize = targetMemoryCacheSize;
            this.bitmapPoolSize = targetPoolSize;
        } else {
            int part = Math.round(((float) maxSize) / 6.0f);
            this.memoryCacheSize = part * 2;
            this.bitmapPoolSize = part * 4;
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Calculated memory cache size: " + toMb(this.memoryCacheSize) + " pool size: " + toMb(this.bitmapPoolSize) + " memory class limited? " + (targetMemoryCacheSize + targetPoolSize > maxSize) + " max size: " + toMb(maxSize) + " memoryClass: " + activityManager.getMemoryClass() + " isLowMemoryDevice: " + isLowMemoryDevice(activityManager));
        }
    }

    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }

    public int getBitmapPoolSize() {
        return this.bitmapPoolSize;
    }

    private static int getMaxSize(ActivityManager activityManager) {
        return Math.round((isLowMemoryDevice(activityManager) ? LOW_MEMORY_MAX_SIZE_MULTIPLIER : MAX_SIZE_MULTIPLIER) * ((float) ((activityManager.getMemoryClass() * 1024) * 1024)));
    }

    private String toMb(int bytes) {
        return Formatter.formatFileSize(this.context, (long) bytes);
    }

    @TargetApi(19)
    private static boolean isLowMemoryDevice(ActivityManager activityManager) {
        if (VERSION.SDK_INT >= 19) {
            return activityManager.isLowRamDevice();
        }
        return VERSION.SDK_INT < 11;
    }
}
