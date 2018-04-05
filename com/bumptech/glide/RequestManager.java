package com.bumptech.glide;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;
import com.bumptech.glide.load.model.stream.MediaStoreStreamLoader;
import com.bumptech.glide.load.model.stream.StreamByteArrayLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class RequestManager implements LifecycleListener {
    private final Context context;
    private final Glide glide;
    private final Lifecycle lifecycle;
    private DefaultOptions options;
    private final OptionsApplier optionsApplier;
    private final RequestTracker requestTracker;
    private final RequestManagerTreeNode treeNode;

    public interface DefaultOptions {
        <T> void apply(GenericRequestBuilder<T, ?, ?, ?> genericRequestBuilder);
    }

    public final class GenericModelRequest<A, T> {
        private final Class<T> dataClass;
        private final ModelLoader<A, T> modelLoader;

        public final class GenericTypeRequest {
            private final A model;
            private final Class<A> modelClass;
            private final boolean providedModel;

            GenericTypeRequest(A model) {
                this.providedModel = true;
                this.model = model;
                this.modelClass = RequestManager.getSafeClass(model);
            }

            GenericTypeRequest(Class<A> modelClass) {
                this.providedModel = false;
                this.model = null;
                this.modelClass = modelClass;
            }

            public <Z> GenericTranscodeRequest<A, T, Z> as(Class<Z> resourceClass) {
                GenericTranscodeRequest<A, T, Z> result = (GenericTranscodeRequest) RequestManager.this.optionsApplier.apply(new GenericTranscodeRequest(RequestManager.this.context, RequestManager.this.glide, this.modelClass, GenericModelRequest.this.modelLoader, GenericModelRequest.this.dataClass, resourceClass, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier));
                if (this.providedModel) {
                    result.load(this.model);
                }
                return result;
            }
        }

        GenericModelRequest(ModelLoader<A, T> modelLoader, Class<T> dataClass) {
            this.modelLoader = modelLoader;
            this.dataClass = dataClass;
        }

        public GenericTypeRequest from(Class<A> modelClass) {
            return new GenericTypeRequest((Class) modelClass);
        }

        public GenericTypeRequest load(A model) {
            return new GenericTypeRequest((Object) model);
        }
    }

    public final class ImageModelRequest<T> {
        private final ModelLoader<T, InputStream> loader;

        ImageModelRequest(ModelLoader<T, InputStream> loader) {
            this.loader = loader;
        }

        public DrawableTypeRequest<T> from(Class<T> modelClass) {
            return (DrawableTypeRequest) RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(modelClass, this.loader, null, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier));
        }

        public DrawableTypeRequest<T> load(T model) {
            return (DrawableTypeRequest) from(RequestManager.getSafeClass(model)).load((Object) model);
        }
    }

    class OptionsApplier {
        OptionsApplier() {
        }

        public <A, X extends GenericRequestBuilder<A, ?, ?, ?>> X apply(X builder) {
            if (RequestManager.this.options != null) {
                RequestManager.this.options.apply(builder);
            }
            return builder;
        }
    }

    public final class VideoModelRequest<T> {
        private final ModelLoader<T, ParcelFileDescriptor> loader;

        VideoModelRequest(ModelLoader<T, ParcelFileDescriptor> loader) {
            this.loader = loader;
        }

        public DrawableTypeRequest<T> load(T model) {
            return (DrawableTypeRequest) ((DrawableTypeRequest) RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(RequestManager.getSafeClass(model), null, this.loader, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier))).load((Object) model);
        }
    }

    private static class RequestManagerConnectivityListener implements ConnectivityListener {
        private final RequestTracker requestTracker;

        public RequestManagerConnectivityListener(RequestTracker requestTracker) {
            this.requestTracker = requestTracker;
        }

        public void onConnectivityChanged(boolean isConnected) {
            if (isConnected) {
                this.requestTracker.restartRequests();
            }
        }
    }

    public RequestManager(Context context, Lifecycle lifecycle, RequestManagerTreeNode treeNode) {
        this(context, lifecycle, treeNode, new RequestTracker(), new ConnectivityMonitorFactory());
    }

    RequestManager(Context context, final Lifecycle lifecycle, RequestManagerTreeNode treeNode, RequestTracker requestTracker, ConnectivityMonitorFactory factory) {
        this.context = context.getApplicationContext();
        this.lifecycle = lifecycle;
        this.treeNode = treeNode;
        this.requestTracker = requestTracker;
        this.glide = Glide.get(context);
        this.optionsApplier = new OptionsApplier();
        ConnectivityMonitor connectivityMonitor = factory.build(context, new RequestManagerConnectivityListener(requestTracker));
        if (Util.isOnBackgroundThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    lifecycle.addListener(RequestManager.this);
                }
            });
        } else {
            lifecycle.addListener(this);
        }
        lifecycle.addListener(connectivityMonitor);
    }

    public void onTrimMemory(int level) {
        this.glide.trimMemory(level);
    }

    public void onLowMemory() {
        this.glide.clearMemory();
    }

    public void setDefaultOptions(DefaultOptions options) {
        this.options = options;
    }

    public boolean isPaused() {
        Util.assertMainThread();
        return this.requestTracker.isPaused();
    }

    public void pauseRequests() {
        Util.assertMainThread();
        this.requestTracker.pauseRequests();
    }

    public void pauseRequestsRecursive() {
        Util.assertMainThread();
        pauseRequests();
        for (RequestManager requestManager : this.treeNode.getDescendants()) {
            requestManager.pauseRequests();
        }
    }

    public void resumeRequests() {
        Util.assertMainThread();
        this.requestTracker.resumeRequests();
    }

    public void resumeRequestsRecursive() {
        Util.assertMainThread();
        resumeRequests();
        for (RequestManager requestManager : this.treeNode.getDescendants()) {
            requestManager.resumeRequests();
        }
    }

    public void onStart() {
        resumeRequests();
    }

    public void onStop() {
        pauseRequests();
    }

    public void onDestroy() {
        this.requestTracker.clearRequests();
    }

    public <A, T> GenericModelRequest<A, T> using(ModelLoader<A, T> modelLoader, Class<T> dataClass) {
        return new GenericModelRequest(modelLoader, dataClass);
    }

    public <T> ImageModelRequest<T> using(StreamModelLoader<T> modelLoader) {
        return new ImageModelRequest(modelLoader);
    }

    public ImageModelRequest<byte[]> using(StreamByteArrayLoader modelLoader) {
        return new ImageModelRequest(modelLoader);
    }

    public <T> VideoModelRequest<T> using(FileDescriptorModelLoader<T> modelLoader) {
        return new VideoModelRequest(modelLoader);
    }

    public DrawableTypeRequest<String> load(String string) {
        return (DrawableTypeRequest) fromString().load((Object) string);
    }

    public DrawableTypeRequest<String> fromString() {
        return loadGeneric(String.class);
    }

    public DrawableTypeRequest<Uri> load(Uri uri) {
        return (DrawableTypeRequest) fromUri().load((Object) uri);
    }

    public DrawableTypeRequest<Uri> fromUri() {
        return loadGeneric(Uri.class);
    }

    @Deprecated
    public DrawableTypeRequest<Uri> loadFromMediaStore(Uri uri, String mimeType, long dateModified, int orientation) {
        return (DrawableTypeRequest) loadFromMediaStore(uri).signature(new MediaStoreSignature(mimeType, dateModified, orientation));
    }

    public DrawableTypeRequest<Uri> loadFromMediaStore(Uri uri) {
        return (DrawableTypeRequest) fromMediaStore().load((Object) uri);
    }

    public DrawableTypeRequest<Uri> fromMediaStore() {
        return (DrawableTypeRequest) this.optionsApplier.apply(new DrawableTypeRequest(Uri.class, new MediaStoreStreamLoader(this.context, Glide.buildStreamModelLoader(Uri.class, this.context)), Glide.buildFileDescriptorModelLoader(Uri.class, this.context), this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
    }

    public DrawableTypeRequest<File> load(File file) {
        return (DrawableTypeRequest) fromFile().load((Object) file);
    }

    public DrawableTypeRequest<File> fromFile() {
        return loadGeneric(File.class);
    }

    public DrawableTypeRequest<Integer> load(Integer resourceId) {
        return (DrawableTypeRequest) fromResource().load((Object) resourceId);
    }

    public DrawableTypeRequest<Integer> fromResource() {
        return (DrawableTypeRequest) loadGeneric(Integer.class).signature(ApplicationVersionSignature.obtain(this.context));
    }

    @Deprecated
    public DrawableTypeRequest<URL> load(URL url) {
        return (DrawableTypeRequest) fromUrl().load((Object) url);
    }

    @Deprecated
    public DrawableTypeRequest<URL> fromUrl() {
        return loadGeneric(URL.class);
    }

    @Deprecated
    public DrawableTypeRequest<byte[]> load(byte[] model, String id) {
        return (DrawableTypeRequest) load(model).signature(new StringSignature(id));
    }

    public DrawableTypeRequest<byte[]> load(byte[] model) {
        return (DrawableTypeRequest) fromBytes().load((Object) model);
    }

    public DrawableTypeRequest<byte[]> fromBytes() {
        return (DrawableTypeRequest) loadGeneric(byte[].class).signature(new StringSignature(UUID.randomUUID().toString())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
    }

    public <T> DrawableTypeRequest<T> load(T model) {
        return (DrawableTypeRequest) loadGeneric(getSafeClass(model)).load((Object) model);
    }

    public <T> DrawableTypeRequest<T> from(Class<T> modelClass) {
        return loadGeneric(modelClass);
    }

    private <T> DrawableTypeRequest<T> loadGeneric(Class<T> modelClass) {
        ModelLoader<T, InputStream> streamModelLoader = Glide.buildStreamModelLoader((Class) modelClass, this.context);
        ModelLoader<T, ParcelFileDescriptor> fileDescriptorModelLoader = Glide.buildFileDescriptorModelLoader((Class) modelClass, this.context);
        if (modelClass != null && streamModelLoader == null && fileDescriptorModelLoader == null) {
            throw new IllegalArgumentException("Unknown type " + modelClass + ". You must provide a Model of a type for" + " which there is a registered ModelLoader, if you are using a custom model, you must first call" + " Glide#register with a ModelLoaderFactory for your custom model class");
        }
        return (DrawableTypeRequest) this.optionsApplier.apply(new DrawableTypeRequest(modelClass, streamModelLoader, fileDescriptorModelLoader, this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
    }

    private static <T> Class<T> getSafeClass(T model) {
        return model != null ? model.getClass() : null;
    }
}
