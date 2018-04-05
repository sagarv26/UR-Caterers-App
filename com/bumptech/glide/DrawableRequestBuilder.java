package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation.Animator;
import com.bumptech.glide.request.target.Target;
import java.io.File;

public class DrawableRequestBuilder<ModelType> extends GenericRequestBuilder<ModelType, ImageVideoWrapper, GifBitmapWrapper, GlideDrawable> implements BitmapOptions, DrawableOptions {
    DrawableRequestBuilder(Context context, Class<ModelType> modelClass, LoadProvider<ModelType, ImageVideoWrapper, GifBitmapWrapper, GlideDrawable> loadProvider, Glide glide, RequestTracker requestTracker, Lifecycle lifecycle) {
        super(context, modelClass, loadProvider, GlideDrawable.class, glide, requestTracker, lifecycle);
        crossFade();
    }

    public DrawableRequestBuilder<ModelType> thumbnail(DrawableRequestBuilder<?> thumbnailRequest) {
        super.thumbnail((GenericRequestBuilder) thumbnailRequest);
        return this;
    }

    public DrawableRequestBuilder<ModelType> thumbnail(GenericRequestBuilder<?, ?, ?, GlideDrawable> thumbnailRequest) {
        super.thumbnail((GenericRequestBuilder) thumbnailRequest);
        return this;
    }

    public DrawableRequestBuilder<ModelType> thumbnail(float sizeMultiplier) {
        super.thumbnail(sizeMultiplier);
        return this;
    }

    public DrawableRequestBuilder<ModelType> sizeMultiplier(float sizeMultiplier) {
        super.sizeMultiplier(sizeMultiplier);
        return this;
    }

    public DrawableRequestBuilder<ModelType> decoder(ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> decoder) {
        super.decoder(decoder);
        return this;
    }

    public DrawableRequestBuilder<ModelType> cacheDecoder(ResourceDecoder<File, GifBitmapWrapper> cacheDecoder) {
        super.cacheDecoder(cacheDecoder);
        return this;
    }

    public DrawableRequestBuilder<ModelType> encoder(ResourceEncoder<GifBitmapWrapper> encoder) {
        super.encoder(encoder);
        return this;
    }

    public DrawableRequestBuilder<ModelType> priority(Priority priority) {
        super.priority(priority);
        return this;
    }

    public DrawableRequestBuilder<ModelType> transform(BitmapTransformation... transformations) {
        return bitmapTransform(transformations);
    }

    public DrawableRequestBuilder<ModelType> centerCrop() {
        return transform(this.glide.getDrawableCenterCrop());
    }

    public DrawableRequestBuilder<ModelType> fitCenter() {
        return transform(this.glide.getDrawableFitCenter());
    }

    public DrawableRequestBuilder<ModelType> bitmapTransform(Transformation<Bitmap>... bitmapTransformations) {
        Transformation[] transformations = new GifBitmapWrapperTransformation[bitmapTransformations.length];
        for (int i = 0; i < bitmapTransformations.length; i++) {
            transformations[i] = new GifBitmapWrapperTransformation(this.glide.getBitmapPool(), bitmapTransformations[i]);
        }
        return transform(transformations);
    }

    public DrawableRequestBuilder<ModelType> transform(Transformation<GifBitmapWrapper>... transformation) {
        super.transform(transformation);
        return this;
    }

    public DrawableRequestBuilder<ModelType> transcoder(ResourceTranscoder<GifBitmapWrapper, GlideDrawable> transcoder) {
        super.transcoder(transcoder);
        return this;
    }

    public final DrawableRequestBuilder<ModelType> crossFade() {
        super.animate(new DrawableCrossFadeFactory());
        return this;
    }

    public DrawableRequestBuilder<ModelType> crossFade(int duration) {
        super.animate(new DrawableCrossFadeFactory(duration));
        return this;
    }

    @Deprecated
    public DrawableRequestBuilder<ModelType> crossFade(Animation animation, int duration) {
        super.animate(new DrawableCrossFadeFactory(animation, duration));
        return this;
    }

    public DrawableRequestBuilder<ModelType> crossFade(int animationId, int duration) {
        super.animate(new DrawableCrossFadeFactory(this.context, animationId, duration));
        return this;
    }

    public DrawableRequestBuilder<ModelType> dontAnimate() {
        super.dontAnimate();
        return this;
    }

    public DrawableRequestBuilder<ModelType> animate(Animator animator) {
        super.animate(animator);
        return this;
    }

    public DrawableRequestBuilder<ModelType> animate(int animationId) {
        super.animate(animationId);
        return this;
    }

    @Deprecated
    public DrawableRequestBuilder<ModelType> animate(Animation animation) {
        super.animate(animation);
        return this;
    }

    public DrawableRequestBuilder<ModelType> placeholder(int resourceId) {
        super.placeholder(resourceId);
        return this;
    }

    public DrawableRequestBuilder<ModelType> placeholder(Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }

    public DrawableRequestBuilder<ModelType> fallback(Drawable drawable) {
        super.fallback(drawable);
        return this;
    }

    public DrawableRequestBuilder<ModelType> fallback(int resourceId) {
        super.fallback(resourceId);
        return this;
    }

    public DrawableRequestBuilder<ModelType> error(int resourceId) {
        super.error(resourceId);
        return this;
    }

    public DrawableRequestBuilder<ModelType> error(Drawable drawable) {
        super.error(drawable);
        return this;
    }

    public DrawableRequestBuilder<ModelType> listener(RequestListener<? super ModelType, GlideDrawable> requestListener) {
        super.listener(requestListener);
        return this;
    }

    public DrawableRequestBuilder<ModelType> diskCacheStrategy(DiskCacheStrategy strategy) {
        super.diskCacheStrategy(strategy);
        return this;
    }

    public DrawableRequestBuilder<ModelType> skipMemoryCache(boolean skip) {
        super.skipMemoryCache(skip);
        return this;
    }

    public DrawableRequestBuilder<ModelType> override(int width, int height) {
        super.override(width, height);
        return this;
    }

    public DrawableRequestBuilder<ModelType> sourceEncoder(Encoder<ImageVideoWrapper> sourceEncoder) {
        super.sourceEncoder(sourceEncoder);
        return this;
    }

    public DrawableRequestBuilder<ModelType> dontTransform() {
        super.dontTransform();
        return this;
    }

    public DrawableRequestBuilder<ModelType> signature(Key signature) {
        super.signature(signature);
        return this;
    }

    public DrawableRequestBuilder<ModelType> load(ModelType model) {
        super.load(model);
        return this;
    }

    public DrawableRequestBuilder<ModelType> clone() {
        return (DrawableRequestBuilder) super.clone();
    }

    public Target<GlideDrawable> into(ImageView view) {
        return super.into(view);
    }

    void applyFitCenter() {
        fitCenter();
    }

    void applyCenterCrop() {
        centerCrop();
    }
}
