package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.model.ImageVideoModelLoader;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.FixedLoadProvider;
import java.io.InputStream;

public class BitmapTypeRequest<ModelType> extends BitmapRequestBuilder<ModelType, Bitmap> {
    private final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader;
    private final Glide glide;
    private final OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    private static <A, R> FixedLoadProvider<A, ImageVideoWrapper, Bitmap, R> buildProvider(Glide glide, ModelLoader<A, InputStream> streamModelLoader, ModelLoader<A, ParcelFileDescriptor> fileDescriptorModelLoader, Class<R> transcodedClass, ResourceTranscoder<Bitmap, R> transcoder) {
        if (streamModelLoader == null && fileDescriptorModelLoader == null) {
            return null;
        }
        if (transcoder == null) {
            transcoder = glide.buildTranscoder(Bitmap.class, transcodedClass);
        }
        return new FixedLoadProvider(new ImageVideoModelLoader(streamModelLoader, fileDescriptorModelLoader), transcoder, glide.buildDataProvider(ImageVideoWrapper.class, Bitmap.class));
    }

    BitmapTypeRequest(GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, InputStream> streamModelLoader, ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader, OptionsApplier optionsApplier) {
        super(buildProvider(other.glide, streamModelLoader, fileDescriptorModelLoader, Bitmap.class, null), Bitmap.class, other);
        this.streamModelLoader = streamModelLoader;
        this.fileDescriptorModelLoader = fileDescriptorModelLoader;
        this.glide = other.glide;
        this.optionsApplier = optionsApplier;
    }

    public <R> BitmapRequestBuilder<ModelType, R> transcode(ResourceTranscoder<Bitmap, R> transcoder, Class<R> transcodeClass) {
        return (BitmapRequestBuilder) this.optionsApplier.apply(new BitmapRequestBuilder(buildProvider(this.glide, this.streamModelLoader, this.fileDescriptorModelLoader, transcodeClass, transcoder), transcodeClass, this));
    }

    public BitmapRequestBuilder<ModelType, byte[]> toBytes() {
        return transcode(new BitmapBytesTranscoder(), byte[].class);
    }

    public BitmapRequestBuilder<ModelType, byte[]> toBytes(CompressFormat compressFormat, int quality) {
        return transcode(new BitmapBytesTranscoder(compressFormat, quality), byte[].class);
    }
}
