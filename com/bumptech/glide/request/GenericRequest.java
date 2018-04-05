package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Engine.LoadStatus;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public final class GenericRequest<A, T, Z, R> implements Request, SizeReadyCallback, ResourceCallback {
    private static final Queue<GenericRequest<?, ?, ?, ?>> REQUEST_POOL = Util.createQueue(0);
    private static final String TAG = "GenericRequest";
    private static final double TO_MEGABYTE = 9.5367431640625E-7d;
    private GlideAnimationFactory<R> animationFactory;
    private Context context;
    private DiskCacheStrategy diskCacheStrategy;
    private Engine engine;
    private Drawable errorDrawable;
    private int errorResourceId;
    private Drawable fallbackDrawable;
    private int fallbackResourceId;
    private boolean isMemoryCacheable;
    private LoadProvider<A, T, Z, R> loadProvider;
    private LoadStatus loadStatus;
    private boolean loadedFromMemoryCache;
    private A model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderResourceId;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<? super A, R> requestListener;
    private Resource<?> resource;
    private Key signature;
    private float sizeMultiplier;
    private long startTime;
    private Status status;
    private final String tag = String.valueOf(hashCode());
    private Target<R> target;
    private Class<R> transcodeClass;
    private Transformation<Z> transformation;

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED
    }

    public static <A, T, Z, R> GenericRequest<A, T, Z, R> obtain(LoadProvider<A, T, Z, R> loadProvider, A model, Key signature, Context context, Priority priority, Target<R> target, float sizeMultiplier, Drawable placeholderDrawable, int placeholderResourceId, Drawable errorDrawable, int errorResourceId, Drawable fallbackDrawable, int fallbackResourceId, RequestListener<? super A, R> requestListener, RequestCoordinator requestCoordinator, Engine engine, Transformation<Z> transformation, Class<R> transcodeClass, boolean isMemoryCacheable, GlideAnimationFactory<R> animationFactory, int overrideWidth, int overrideHeight, DiskCacheStrategy diskCacheStrategy) {
        GenericRequest<A, T, Z, R> request = (GenericRequest) REQUEST_POOL.poll();
        if (request == null) {
            request = new GenericRequest();
        }
        request.init(loadProvider, model, signature, context, priority, target, sizeMultiplier, placeholderDrawable, placeholderResourceId, errorDrawable, errorResourceId, fallbackDrawable, fallbackResourceId, requestListener, requestCoordinator, engine, transformation, transcodeClass, isMemoryCacheable, animationFactory, overrideWidth, overrideHeight, diskCacheStrategy);
        return request;
    }

    private GenericRequest() {
    }

    public void recycle() {
        this.loadProvider = null;
        this.model = null;
        this.context = null;
        this.target = null;
        this.placeholderDrawable = null;
        this.errorDrawable = null;
        this.fallbackDrawable = null;
        this.requestListener = null;
        this.requestCoordinator = null;
        this.transformation = null;
        this.animationFactory = null;
        this.loadedFromMemoryCache = false;
        this.loadStatus = null;
        REQUEST_POOL.offer(this);
    }

    private void init(LoadProvider<A, T, Z, R> loadProvider, A model, Key signature, Context context, Priority priority, Target<R> target, float sizeMultiplier, Drawable placeholderDrawable, int placeholderResourceId, Drawable errorDrawable, int errorResourceId, Drawable fallbackDrawable, int fallbackResourceId, RequestListener<? super A, R> requestListener, RequestCoordinator requestCoordinator, Engine engine, Transformation<Z> transformation, Class<R> transcodeClass, boolean isMemoryCacheable, GlideAnimationFactory<R> animationFactory, int overrideWidth, int overrideHeight, DiskCacheStrategy diskCacheStrategy) {
        this.loadProvider = loadProvider;
        this.model = model;
        this.signature = signature;
        this.fallbackDrawable = fallbackDrawable;
        this.fallbackResourceId = fallbackResourceId;
        this.context = context.getApplicationContext();
        this.priority = priority;
        this.target = target;
        this.sizeMultiplier = sizeMultiplier;
        this.placeholderDrawable = placeholderDrawable;
        this.placeholderResourceId = placeholderResourceId;
        this.errorDrawable = errorDrawable;
        this.errorResourceId = errorResourceId;
        this.requestListener = requestListener;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.transformation = transformation;
        this.transcodeClass = transcodeClass;
        this.isMemoryCacheable = isMemoryCacheable;
        this.animationFactory = animationFactory;
        this.overrideWidth = overrideWidth;
        this.overrideHeight = overrideHeight;
        this.diskCacheStrategy = diskCacheStrategy;
        this.status = Status.PENDING;
        if (model != null) {
            check("ModelLoader", loadProvider.getModelLoader(), "try .using(ModelLoader)");
            check("Transcoder", loadProvider.getTranscoder(), "try .as*(Class).transcode(ResourceTranscoder)");
            check("Transformation", transformation, "try .transform(UnitTransformation.get())");
            if (diskCacheStrategy.cacheSource()) {
                check("SourceEncoder", loadProvider.getSourceEncoder(), "try .sourceEncoder(Encoder) or .diskCacheStrategy(NONE/RESULT)");
            } else {
                check("SourceDecoder", loadProvider.getSourceDecoder(), "try .decoder/.imageDecoder/.videoDecoder(ResourceDecoder) or .diskCacheStrategy(ALL/SOURCE)");
            }
            if (diskCacheStrategy.cacheSource() || diskCacheStrategy.cacheResult()) {
                check("CacheDecoder", loadProvider.getCacheDecoder(), "try .cacheDecoder(ResouceDecoder) or .diskCacheStrategy(NONE)");
            }
            if (diskCacheStrategy.cacheResult()) {
                check("Encoder", loadProvider.getEncoder(), "try .encode(ResourceEncoder) or .diskCacheStrategy(NONE/SOURCE)");
            }
        }
    }

    private static void check(String name, Object object, String suggestion) {
        if (object == null) {
            StringBuilder message = new StringBuilder(name);
            message.append(" must not be null");
            if (suggestion != null) {
                message.append(", ");
                message.append(suggestion);
            }
            throw new NullPointerException(message.toString());
        }
    }

    public void begin() {
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            onException(null);
            return;
        }
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            onSizeReady(this.overrideWidth, this.overrideHeight);
        } else {
            this.target.getSize(this);
        }
        if (!(isComplete() || isFailed() || !canNotifyStatusChanged())) {
            this.target.onLoadStarted(getPlaceholderDrawable());
        }
        if (Log.isLoggable(TAG, 2)) {
            logV("finished run method in " + LogTime.getElapsedMillis(this.startTime));
        }
    }

    void cancel() {
        this.status = Status.CANCELLED;
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    public void clear() {
        Util.assertMainThread();
        if (this.status != Status.CLEARED) {
            cancel();
            if (this.resource != null) {
                releaseResource(this.resource);
            }
            if (canNotifyStatusChanged()) {
                this.target.onLoadCleared(getPlaceholderDrawable());
            }
            this.status = Status.CLEARED;
        }
    }

    public boolean isPaused() {
        return this.status == Status.PAUSED;
    }

    public void pause() {
        clear();
        this.status = Status.PAUSED;
    }

    private void releaseResource(Resource resource) {
        this.engine.release(resource);
        this.resource = null;
    }

    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public boolean isResourceSet() {
        return isComplete();
    }

    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }

    public boolean isFailed() {
        return this.status == Status.FAILED;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null && this.fallbackResourceId > 0) {
            this.fallbackDrawable = this.context.getResources().getDrawable(this.fallbackResourceId);
        }
        return this.fallbackDrawable;
    }

    private void setErrorPlaceholder(Exception e) {
        if (canNotifyStatusChanged()) {
            Drawable error = this.model == null ? getFallbackDrawable() : null;
            if (error == null) {
                error = getErrorDrawable();
            }
            if (error == null) {
                error = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(e, error);
        }
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null && this.errorResourceId > 0) {
            this.errorDrawable = this.context.getResources().getDrawable(this.errorResourceId);
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null && this.placeholderResourceId > 0) {
            this.placeholderDrawable = this.context.getResources().getDrawable(this.placeholderResourceId);
        }
        return this.placeholderDrawable;
    }

    public void onSizeReady(int width, int height) {
        if (Log.isLoggable(TAG, 2)) {
            logV("Got onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
        }
        if (this.status == Status.WAITING_FOR_SIZE) {
            this.status = Status.RUNNING;
            width = Math.round(this.sizeMultiplier * ((float) width));
            height = Math.round(this.sizeMultiplier * ((float) height));
            DataFetcher<T> dataFetcher = this.loadProvider.getModelLoader().getResourceFetcher(this.model, width, height);
            if (dataFetcher == null) {
                onException(new Exception("Failed to load model: '" + this.model + "'"));
                return;
            }
            ResourceTranscoder<Z, R> transcoder = this.loadProvider.getTranscoder();
            if (Log.isLoggable(TAG, 2)) {
                logV("finished setup for calling load in " + LogTime.getElapsedMillis(this.startTime));
            }
            this.loadedFromMemoryCache = true;
            this.loadStatus = this.engine.load(this.signature, width, height, dataFetcher, this.loadProvider, this.transformation, transcoder, this.priority, this.isMemoryCacheable, this.diskCacheStrategy, this);
            this.loadedFromMemoryCache = this.resource != null;
            if (Log.isLoggable(TAG, 2)) {
                logV("finished onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
            }
        }
    }

    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }

    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }

    public void onResourceReady(Resource<?> resource) {
        if (resource == null) {
            onException(new Exception("Expected to receive a Resource<R> with an object of " + this.transcodeClass + " inside, but instead got null."));
            return;
        }
        Object received = resource.get();
        if (received == null || !this.transcodeClass.isAssignableFrom(received.getClass())) {
            releaseResource(resource);
            onException(new Exception("Expected to receive an object of " + this.transcodeClass + " but instead got " + (received != null ? received.getClass() : "") + "{" + received + "}" + " inside Resource{" + resource + "}." + (received != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.")));
        } else if (canSetResource()) {
            onResourceReady(resource, received);
        } else {
            releaseResource(resource);
            this.status = Status.COMPLETE;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onResourceReady(com.bumptech.glide.load.engine.Resource<?> r11, R r12) {
        /*
        r10 = this;
        r5 = r10.isFirstReadyResource();
        r0 = com.bumptech.glide.request.GenericRequest.Status.COMPLETE;
        r10.status = r0;
        r10.resource = r11;
        r0 = r10.requestListener;
        if (r0 == 0) goto L_0x001d;
    L_0x000e:
        r0 = r10.requestListener;
        r2 = r10.model;
        r3 = r10.target;
        r4 = r10.loadedFromMemoryCache;
        r1 = r12;
        r0 = r0.onResourceReady(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x002a;
    L_0x001d:
        r0 = r10.animationFactory;
        r1 = r10.loadedFromMemoryCache;
        r6 = r0.build(r1, r5);
        r0 = r10.target;
        r0.onResourceReady(r12, r6);
    L_0x002a:
        r10.notifyLoadSuccess();
        r0 = "GenericRequest";
        r1 = 2;
        r0 = android.util.Log.isLoggable(r0, r1);
        if (r0 == 0) goto L_0x0070;
    L_0x0036:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Resource ready in ";
        r0 = r0.append(r1);
        r2 = r10.startTime;
        r2 = com.bumptech.glide.util.LogTime.getElapsedMillis(r2);
        r0 = r0.append(r2);
        r1 = " size: ";
        r0 = r0.append(r1);
        r1 = r11.getSize();
        r2 = (double) r1;
        r8 = 4517110426252607488; // 0x3eb0000000000000 float:0.0 double:9.5367431640625E-7;
        r2 = r2 * r8;
        r0 = r0.append(r2);
        r1 = " fromCache: ";
        r0 = r0.append(r1);
        r1 = r10.loadedFromMemoryCache;
        r0 = r0.append(r1);
        r0 = r0.toString();
        r10.logV(r0);
    L_0x0070:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.request.GenericRequest.onResourceReady(com.bumptech.glide.load.engine.Resource, java.lang.Object):void");
    }

    public void onException(Exception e) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "load failed", e);
        }
        this.status = Status.FAILED;
        if (this.requestListener == null || !this.requestListener.onException(e, this.model, this.target, isFirstReadyResource())) {
            setErrorPlaceholder(e);
        }
    }

    private void logV(String message) {
        Log.v(TAG, message + " this: " + this.tag);
    }
}
