package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.InputStream;

public class ImageVideoModelLoader<A> implements ModelLoader<A, ImageVideoWrapper> {
    private static final String TAG = "IVML";
    private final ModelLoader<A, ParcelFileDescriptor> fileDescriptorLoader;
    private final ModelLoader<A, InputStream> streamLoader;

    static class ImageVideoFetcher implements DataFetcher<ImageVideoWrapper> {
        private final DataFetcher<ParcelFileDescriptor> fileDescriptorFetcher;
        private final DataFetcher<InputStream> streamFetcher;

        public ImageVideoFetcher(DataFetcher<InputStream> streamFetcher, DataFetcher<ParcelFileDescriptor> fileDescriptorFetcher) {
            this.streamFetcher = streamFetcher;
            this.fileDescriptorFetcher = fileDescriptorFetcher;
        }

        public ImageVideoWrapper loadData(Priority priority) throws Exception {
            InputStream is = null;
            if (this.streamFetcher != null) {
                try {
                    is = (InputStream) this.streamFetcher.loadData(priority);
                } catch (Exception e) {
                    if (Log.isLoggable(ImageVideoModelLoader.TAG, 2)) {
                        Log.v(ImageVideoModelLoader.TAG, "Exception fetching input stream, trying ParcelFileDescriptor", e);
                    }
                    if (this.fileDescriptorFetcher == null) {
                        throw e;
                    }
                }
            }
            ParcelFileDescriptor fileDescriptor = null;
            if (this.fileDescriptorFetcher != null) {
                try {
                    fileDescriptor = (ParcelFileDescriptor) this.fileDescriptorFetcher.loadData(priority);
                } catch (Exception e2) {
                    if (Log.isLoggable(ImageVideoModelLoader.TAG, 2)) {
                        Log.v(ImageVideoModelLoader.TAG, "Exception fetching ParcelFileDescriptor", e2);
                    }
                    if (is == null) {
                        throw e2;
                    }
                }
            }
            return new ImageVideoWrapper(is, fileDescriptor);
        }

        public void cleanup() {
            if (this.streamFetcher != null) {
                this.streamFetcher.cleanup();
            }
            if (this.fileDescriptorFetcher != null) {
                this.fileDescriptorFetcher.cleanup();
            }
        }

        public String getId() {
            if (this.streamFetcher != null) {
                return this.streamFetcher.getId();
            }
            return this.fileDescriptorFetcher.getId();
        }

        public void cancel() {
            if (this.streamFetcher != null) {
                this.streamFetcher.cancel();
            }
            if (this.fileDescriptorFetcher != null) {
                this.fileDescriptorFetcher.cancel();
            }
        }
    }

    public ImageVideoModelLoader(ModelLoader<A, InputStream> streamLoader, ModelLoader<A, ParcelFileDescriptor> fileDescriptorLoader) {
        if (streamLoader == null && fileDescriptorLoader == null) {
            throw new NullPointerException("At least one of streamLoader and fileDescriptorLoader must be non null");
        }
        this.streamLoader = streamLoader;
        this.fileDescriptorLoader = fileDescriptorLoader;
    }

    public DataFetcher<ImageVideoWrapper> getResourceFetcher(A model, int width, int height) {
        DataFetcher<InputStream> streamFetcher = null;
        if (this.streamLoader != null) {
            streamFetcher = this.streamLoader.getResourceFetcher(model, width, height);
        }
        DataFetcher<ParcelFileDescriptor> fileDescriptorFetcher = null;
        if (this.fileDescriptorLoader != null) {
            fileDescriptorFetcher = this.fileDescriptorLoader.getResourceFetcher(model, width, height);
        }
        if (streamFetcher == null && fileDescriptorFetcher == null) {
            return null;
        }
        return new ImageVideoFetcher(streamFetcher, fileDescriptorFetcher);
    }
}
