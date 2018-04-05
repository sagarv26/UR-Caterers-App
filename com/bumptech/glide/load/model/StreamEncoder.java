package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.util.ByteArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamEncoder implements Encoder<InputStream> {
    private static final String TAG = "StreamEncoder";

    public boolean encode(InputStream data, OutputStream os) {
        byte[] buffer = ByteArrayPool.get().getBytes();
        while (true) {
            try {
                int read = data.read(buffer);
                if (read != -1) {
                    os.write(buffer, 0, read);
                } else {
                    ByteArrayPool.get().releaseBytes(buffer);
                    return true;
                }
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Failed to encode data onto the OutputStream", e);
                }
                ByteArrayPool.get().releaseBytes(buffer);
                return false;
            } catch (Throwable th) {
                ByteArrayPool.get().releaseBytes(buffer);
            }
        }
    }

    public String getId() {
        return "";
    }
}
