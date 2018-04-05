package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.util.Util;

public class GifDrawableResource extends DrawableResource<GifDrawable> {
    public GifDrawableResource(GifDrawable drawable) {
        super(drawable);
    }

    public int getSize() {
        return Util.getBitmapByteSize(((GifDrawable) this.drawable).getFirstFrame()) + ((GifDrawable) this.drawable).getData().length;
    }

    public void recycle() {
        ((GifDrawable) this.drawable).stop();
        ((GifDrawable) this.drawable).recycle();
    }
}
