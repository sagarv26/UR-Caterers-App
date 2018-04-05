package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import java.io.IOException;
import java.io.InputStream;

public class ImageVideoBitmapDecoder implements ResourceDecoder<ImageVideoWrapper, Bitmap> {
    private static final String TAG = "ImageVideoDecoder";
    private final ResourceDecoder<ParcelFileDescriptor, Bitmap> fileDescriptorDecoder;
    private final ResourceDecoder<InputStream, Bitmap> streamDecoder;

    public ImageVideoBitmapDecoder(ResourceDecoder<InputStream, Bitmap> streamDecoder, ResourceDecoder<ParcelFileDescriptor, Bitmap> fileDescriptorDecoder) {
        this.streamDecoder = streamDecoder;
        this.fileDescriptorDecoder = fileDescriptorDecoder;
    }

    public Resource<Bitmap> decode(ImageVideoWrapper source, int width, int height) throws IOException {
        Resource<Bitmap> result = null;
        InputStream is = source.getStream();
        if (is != null) {
            try {
                result = this.streamDecoder.decode(is, width, height);
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "Failed to load image from stream, trying FileDescriptor", e);
                }
            }
        }
        if (result != null) {
            return result;
        }
        ParcelFileDescriptor fileDescriptor = source.getFileDescriptor();
        if (fileDescriptor != null) {
            return this.fileDescriptorDecoder.decode(fileDescriptor, width, height);
        }
        return result;
    }

    public String getId() {
        return "ImageVideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
    }
}
