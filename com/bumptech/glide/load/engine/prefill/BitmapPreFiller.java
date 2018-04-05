package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.PreFillType.Builder;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public final class BitmapPreFiller {
    private final BitmapPool bitmapPool;
    private BitmapPreFillRunner current;
    private final DecodeFormat defaultFormat;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final MemoryCache memoryCache;

    public BitmapPreFiller(MemoryCache memoryCache, BitmapPool bitmapPool, DecodeFormat defaultFormat) {
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.defaultFormat = defaultFormat;
    }

    public void preFill(Builder... bitmapAttributeBuilders) {
        if (this.current != null) {
            this.current.cancel();
        }
        PreFillType[] bitmapAttributes = new PreFillType[bitmapAttributeBuilders.length];
        for (int i = 0; i < bitmapAttributeBuilders.length; i++) {
            Builder builder = bitmapAttributeBuilders[i];
            if (builder.getConfig() == null) {
                Config config = (this.defaultFormat == DecodeFormat.ALWAYS_ARGB_8888 || this.defaultFormat == DecodeFormat.PREFER_ARGB_8888) ? Config.ARGB_8888 : Config.RGB_565;
                builder.setConfig(config);
            }
            bitmapAttributes[i] = builder.build();
        }
        this.current = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, generateAllocationOrder(bitmapAttributes));
        this.handler.post(this.current);
    }

    PreFillQueue generateAllocationOrder(PreFillType[] preFillSizes) {
        int maxSize = (this.memoryCache.getMaxSize() - this.memoryCache.getCurrentSize()) + this.bitmapPool.getMaxSize();
        int totalWeight = 0;
        for (PreFillType size : preFillSizes) {
            totalWeight += size.getWeight();
        }
        float bytesPerWeight = ((float) maxSize) / ((float) totalWeight);
        Map<PreFillType, Integer> attributeToCount = new HashMap();
        for (PreFillType size2 : preFillSizes) {
            attributeToCount.put(size2, Integer.valueOf(Math.round(((float) size2.getWeight()) * bytesPerWeight) / getSizeInBytes(size2)));
        }
        return new PreFillQueue(attributeToCount);
    }

    private static int getSizeInBytes(PreFillType size) {
        return Util.getBitmapByteSize(size.getWidth(), size.getHeight(), size.getConfig());
    }
}
