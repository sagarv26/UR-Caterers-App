package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class GifDrawableTransformation implements Transformation<GifDrawable> {
    private final BitmapPool bitmapPool;
    private final Transformation<Bitmap> wrapped;

    public GifDrawableTransformation(Transformation<Bitmap> wrapped, BitmapPool bitmapPool) {
        this.wrapped = wrapped;
        this.bitmapPool = bitmapPool;
    }

    public Resource<GifDrawable> transform(Resource<GifDrawable> resource, int outWidth, int outHeight) {
        GifDrawable drawable = (GifDrawable) resource.get();
        Bitmap firstFrame = ((GifDrawable) resource.get()).getFirstFrame();
        Bitmap transformedFrame = (Bitmap) this.wrapped.transform(new BitmapResource(firstFrame, this.bitmapPool), outWidth, outHeight).get();
        if (transformedFrame.equals(firstFrame)) {
            return resource;
        }
        return new GifDrawableResource(new GifDrawable(drawable, transformedFrame, this.wrapped));
    }

    public String getId() {
        return this.wrapped.getId();
    }
}
