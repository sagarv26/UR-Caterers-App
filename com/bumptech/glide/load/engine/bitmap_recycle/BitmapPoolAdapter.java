package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class BitmapPoolAdapter implements BitmapPool {
    public int getMaxSize() {
        return 0;
    }

    public void setSizeMultiplier(float sizeMultiplier) {
    }

    public boolean put(Bitmap bitmap) {
        return false;
    }

    public Bitmap get(int width, int height, Config config) {
        return null;
    }

    public Bitmap getDirty(int width, int height, Config config) {
        return null;
    }

    public void clearMemory() {
    }

    public void trimMemory(int level) {
    }
}
