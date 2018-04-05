package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class FitCenter extends BitmapTransformation {
    public FitCenter(Context context) {
        super(context);
    }

    public FitCenter(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.fitCenter(toTransform, pool, outWidth, outHeight);
    }

    public String getId() {
        return "FitCenter.com.bumptech.glide.load.resource.bitmap";
    }
}
