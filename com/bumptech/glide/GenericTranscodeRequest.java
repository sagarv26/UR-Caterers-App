package com.bumptech.glide;

import android.content.Context;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;

public class GenericTranscodeRequest<ModelType, DataType, ResourceType> extends GenericRequestBuilder<ModelType, DataType, ResourceType, ResourceType> implements DownloadOptions {
    private final Class<DataType> dataClass;
    private final ModelLoader<ModelType, DataType> modelLoader;
    private final OptionsApplier optionsApplier;
    private final Class<ResourceType> resourceClass;

    private static <A, T, Z, R> LoadProvider<A, T, Z, R> build(Glide glide, ModelLoader<A, T> modelLoader, Class<T> dataClass, Class<Z> resourceClass, ResourceTranscoder<Z, R> transcoder) {
        return new FixedLoadProvider(modelLoader, transcoder, glide.buildDataProvider(dataClass, resourceClass));
    }

    GenericTranscodeRequest(Class<ResourceType> transcodeClass, GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, DataType> modelLoader, Class<DataType> dataClass, Class<ResourceType> resourceClass, OptionsApplier optionsApplier) {
        super(build(other.glide, modelLoader, dataClass, resourceClass, UnitTranscoder.get()), transcodeClass, other);
        this.modelLoader = modelLoader;
        this.dataClass = dataClass;
        this.resourceClass = resourceClass;
        this.optionsApplier = optionsApplier;
    }

    GenericTranscodeRequest(Context context, Glide glide, Class<ModelType> modelClass, ModelLoader<ModelType, DataType> modelLoader, Class<DataType> dataClass, Class<ResourceType> resourceClass, RequestTracker requestTracker, Lifecycle lifecycle, OptionsApplier optionsApplier) {
        super(context, modelClass, build(glide, modelLoader, dataClass, resourceClass, UnitTranscoder.get()), resourceClass, glide, requestTracker, lifecycle);
        this.modelLoader = modelLoader;
        this.dataClass = dataClass;
        this.resourceClass = resourceClass;
        this.optionsApplier = optionsApplier;
    }

    public <TranscodeType> GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transcode(ResourceTranscoder<ResourceType, TranscodeType> transcoder, Class<TranscodeType> transcodeClass) {
        return this.optionsApplier.apply(new GenericRequestBuilder(build(this.glide, this.modelLoader, this.dataClass, this.resourceClass, transcoder), transcodeClass, this));
    }

    public <Y extends Target<File>> Y downloadOnly(Y target) {
        return getDownloadOnlyRequest().into((Target) target);
    }

    public FutureTarget<File> downloadOnly(int width, int height) {
        return getDownloadOnlyRequest().into(width, height);
    }

    private GenericRequestBuilder<ModelType, DataType, File, File> getDownloadOnlyRequest() {
        return this.optionsApplier.apply(new GenericRequestBuilder(new FixedLoadProvider(this.modelLoader, UnitTranscoder.get(), this.glide.buildDataProvider(this.dataClass, File.class)), File.class, this)).priority(Priority.LOW).diskCacheStrategy(DiskCacheStrategy.SOURCE).skipMemoryCache(true);
    }
}
