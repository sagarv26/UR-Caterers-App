package com.bumptech.glide;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.FixedLoadProvider;
import java.io.InputStream;

public class GifTypeRequest<ModelType> extends GifRequestBuilder<ModelType> {
    private final OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    private static <A, R> FixedLoadProvider<A, InputStream, GifDrawable, R> buildProvider(Glide glide, ModelLoader<A, InputStream> streamModelLoader, Class<R> transcodeClass, ResourceTranscoder<GifDrawable, R> transcoder) {
        if (streamModelLoader == null) {
            return null;
        }
        if (transcoder == null) {
            transcoder = glide.buildTranscoder(GifDrawable.class, transcodeClass);
        }
        return new FixedLoadProvider(streamModelLoader, transcoder, glide.buildDataProvider(InputStream.class, GifDrawable.class));
    }

    GifTypeRequest(GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, InputStream> streamModelLoader, OptionsApplier optionsApplier) {
        super(buildProvider(other.glide, streamModelLoader, GifDrawable.class, null), GifDrawable.class, other);
        this.streamModelLoader = streamModelLoader;
        this.optionsApplier = optionsApplier;
        crossFade();
    }

    public <R> GenericRequestBuilder<ModelType, InputStream, GifDrawable, R> transcode(ResourceTranscoder<GifDrawable, R> transcoder, Class<R> transcodeClass) {
        return this.optionsApplier.apply(new GenericRequestBuilder(buildProvider(this.glide, this.streamModelLoader, transcodeClass, transcoder), transcodeClass, this));
    }

    public GenericRequestBuilder<ModelType, InputStream, GifDrawable, byte[]> toBytes() {
        return transcode(new GifDrawableBytesTranscoder(), byte[].class);
    }
}
