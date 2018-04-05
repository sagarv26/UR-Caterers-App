package com.bumptech.glide;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.model.ImageVideoModelLoader;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.InputStream;

public class DrawableTypeRequest<ModelType> extends DrawableRequestBuilder<ModelType> implements DownloadOptions {
    private final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader;
    private final OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    private static <A, Z, R> FixedLoadProvider<A, ImageVideoWrapper, Z, R> buildProvider(Glide glide, ModelLoader<A, InputStream> streamModelLoader, ModelLoader<A, ParcelFileDescriptor> fileDescriptorModelLoader, Class<Z> resourceClass, Class<R> transcodedClass, ResourceTranscoder<Z, R> transcoder) {
        if (streamModelLoader == null && fileDescriptorModelLoader == null) {
            return null;
        }
        if (transcoder == null) {
            transcoder = glide.buildTranscoder(resourceClass, transcodedClass);
        }
        return new FixedLoadProvider(new ImageVideoModelLoader(streamModelLoader, fileDescriptorModelLoader), transcoder, glide.buildDataProvider(ImageVideoWrapper.class, resourceClass));
    }

    DrawableTypeRequest(Class<ModelType> modelClass, ModelLoader<ModelType, InputStream> streamModelLoader, ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader, Context context, Glide glide, RequestTracker requestTracker, Lifecycle lifecycle, OptionsApplier optionsApplier) {
        super(context, modelClass, buildProvider(glide, streamModelLoader, fileDescriptorModelLoader, GifBitmapWrapper.class, GlideDrawable.class, null), glide, requestTracker, lifecycle);
        this.streamModelLoader = streamModelLoader;
        this.fileDescriptorModelLoader = fileDescriptorModelLoader;
        this.optionsApplier = optionsApplier;
    }

    public BitmapTypeRequest<ModelType> asBitmap() {
        return (BitmapTypeRequest) this.optionsApplier.apply(new BitmapTypeRequest(this, this.streamModelLoader, this.fileDescriptorModelLoader, this.optionsApplier));
    }

    public GifTypeRequest<ModelType> asGif() {
        return (GifTypeRequest) this.optionsApplier.apply(new GifTypeRequest(this, this.streamModelLoader, this.optionsApplier));
    }

    public <Y extends Target<File>> Y downloadOnly(Y target) {
        return getDownloadOnlyRequest().downloadOnly(target);
    }

    public FutureTarget<File> downloadOnly(int width, int height) {
        return getDownloadOnlyRequest().downloadOnly(width, height);
    }

    private GenericTranscodeRequest<ModelType, InputStream, File> getDownloadOnlyRequest() {
        return (GenericTranscodeRequest) this.optionsApplier.apply(new GenericTranscodeRequest(File.class, this, this.streamModelLoader, InputStream.class, File.class, this.optionsApplier));
    }
}
