package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;

public class VideoBitmapDecoder implements BitmapDecoder<ParcelFileDescriptor> {
    private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
    private static final int NO_FRAME = -1;
    private MediaMetadataRetrieverFactory factory;
    private int frame;

    static class MediaMetadataRetrieverFactory {
        MediaMetadataRetrieverFactory() {
        }

        public MediaMetadataRetriever build() {
            return new MediaMetadataRetriever();
        }
    }

    public VideoBitmapDecoder() {
        this(DEFAULT_FACTORY, -1);
    }

    public VideoBitmapDecoder(int frame) {
        this(DEFAULT_FACTORY, checkValidFrame(frame));
    }

    VideoBitmapDecoder(MediaMetadataRetrieverFactory factory) {
        this(factory, -1);
    }

    VideoBitmapDecoder(MediaMetadataRetrieverFactory factory, int frame) {
        this.factory = factory;
        this.frame = frame;
    }

    public Bitmap decode(ParcelFileDescriptor resource, BitmapPool bitmapPool, int outWidth, int outHeight, DecodeFormat decodeFormat) throws IOException {
        Bitmap result;
        MediaMetadataRetriever mediaMetadataRetriever = this.factory.build();
        mediaMetadataRetriever.setDataSource(resource.getFileDescriptor());
        if (this.frame >= 0) {
            result = mediaMetadataRetriever.getFrameAtTime((long) this.frame);
        } else {
            result = mediaMetadataRetriever.getFrameAtTime();
        }
        mediaMetadataRetriever.release();
        resource.close();
        return result;
    }

    public String getId() {
        return "VideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
    }

    private static int checkValidFrame(int frame) {
        if (frame >= 0) {
            return frame;
        }
        throw new IllegalArgumentException("Requested frame must be non-negative");
    }
}
