package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class CenterCrop extends BitmapTransformation {
    public CenterCrop(Context context) {
        super(context);
    }

    public CenterCrop(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap toReuse = pool.get(outWidth, outHeight, toTransform.getConfig() != null ? toTransform.getConfig() : Config.ARGB_8888);
        Bitmap transformed = TransformationUtils.centerCrop(toReuse, toTransform, outWidth, outHeight);
        if (!(toReuse == null || toReuse == transformed || pool.put(toReuse))) {
            toReuse.recycle();
        }
        return transformed;
    }

    public String getId() {
        return "CenterCrop.com.bumptech.glide.load.resource.bitmap";
    }
}
