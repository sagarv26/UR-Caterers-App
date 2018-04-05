package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final int DEFAULT_COMPRESSION_QUALITY = 90;
    private static final String TAG = "BitmapEncoder";
    private CompressFormat compressFormat;
    private int quality;

    public BitmapEncoder() {
        this(null, 90);
    }

    public BitmapEncoder(CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    public boolean encode(Resource<Bitmap> resource, OutputStream os) {
        Bitmap bitmap = (Bitmap) resource.get();
        long start = LogTime.getLogTime();
        CompressFormat format = getFormat(bitmap);
        bitmap.compress(format, this.quality, os);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "Compressed with type: " + format + " of size " + Util.getBitmapByteSize(bitmap) + " in " + LogTime.getElapsedMillis(start));
        }
        return true;
    }

    public String getId() {
        return "BitmapEncoder.com.bumptech.glide.load.resource.bitmap";
    }

    private CompressFormat getFormat(Bitmap bitmap) {
        if (this.compressFormat != null) {
            return this.compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return CompressFormat.PNG;
        }
        return CompressFormat.JPEG;
    }
}
