package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

class GifBitmapProvider implements BitmapProvider {
    private final BitmapPool bitmapPool;

    public GifBitmapProvider(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    public Bitmap obtain(int width, int height, Config config) {
        return this.bitmapPool.getDirty(width, height, config);
    }

    public void release(Bitmap bitmap) {
        if (!this.bitmapPool.put(bitmap)) {
            bitmap.recycle();
        }
    }
}
