package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation.Animator;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.InputStream;

public class BitmapRequestBuilder<ModelType, TranscodeType> extends GenericRequestBuilder<ModelType, ImageVideoWrapper, Bitmap, TranscodeType> implements BitmapOptions {
    private final BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;
    private Downsampler downsampler = Downsampler.AT_LEAST;
    private ResourceDecoder<InputStream, Bitmap> imageDecoder;
    private ResourceDecoder<ParcelFileDescriptor, Bitmap> videoDecoder;

    BitmapRequestBuilder(LoadProvider<ModelType, ImageVideoWrapper, Bitmap, TranscodeType> loadProvider, Class<TranscodeType> transcodeClass, GenericRequestBuilder<ModelType, ?, ?, ?> other) {
        super(loadProvider, transcodeClass, other);
        this.bitmapPool = other.glide.getBitmapPool();
        this.decodeFormat = other.glide.getDecodeFormat();
        this.imageDecoder = new StreamBitmapDecoder(this.bitmapPool, this.decodeFormat);
        this.videoDecoder = new FileDescriptorBitmapDecoder(this.bitmapPool, this.decodeFormat);
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> approximate() {
        return downsample(Downsampler.AT_LEAST);
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> asIs() {
        return downsample(Downsampler.NONE);
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> atMost() {
        return downsample(Downsampler.AT_MOST);
    }

    private BitmapRequestBuilder<ModelType, TranscodeType> downsample(Downsampler downsampler) {
        this.downsampler = downsampler;
        this.imageDecoder = new StreamBitmapDecoder(downsampler, this.bitmapPool, this.decodeFormat);
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(float sizeMultiplier) {
        super.thumbnail(sizeMultiplier);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(BitmapRequestBuilder<?, TranscodeType> thumbnailRequest) {
        super.thumbnail((GenericRequestBuilder) thumbnailRequest);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> sizeMultiplier(float sizeMultiplier) {
        super.sizeMultiplier(sizeMultiplier);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> decoder(ResourceDecoder<ImageVideoWrapper, Bitmap> decoder) {
        super.decoder(decoder);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> cacheDecoder(ResourceDecoder<File, Bitmap> cacheDecoder) {
        super.cacheDecoder(cacheDecoder);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> encoder(ResourceEncoder<Bitmap> encoder) {
        super.encoder(encoder);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> imageDecoder(ResourceDecoder<InputStream, Bitmap> decoder) {
        this.imageDecoder = decoder;
        super.decoder(new ImageVideoBitmapDecoder(decoder, this.videoDecoder));
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> videoDecoder(ResourceDecoder<ParcelFileDescriptor, Bitmap> decoder) {
        this.videoDecoder = decoder;
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, decoder));
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> format(DecodeFormat format) {
        this.decodeFormat = format;
        this.imageDecoder = new StreamBitmapDecoder(this.downsampler, this.bitmapPool, format);
        this.videoDecoder = new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(), this.bitmapPool, format);
        super.cacheDecoder(new FileToStreamDecoder(new StreamBitmapDecoder(this.downsampler, this.bitmapPool, format)));
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> priority(Priority priority) {
        super.priority(priority);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> transform(BitmapTransformation... transformations) {
        super.transform(transformations);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> centerCrop() {
        return transform(this.glide.getBitmapCenterCrop());
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> fitCenter() {
        return transform(this.glide.getBitmapFitCenter());
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> transform(Transformation<Bitmap>... transformations) {
        super.transform(transformations);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> transcoder(ResourceTranscoder<Bitmap, TranscodeType> transcoder) {
        super.transcoder(transcoder);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> dontAnimate() {
        super.dontAnimate();
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> animate(int animationId) {
        super.animate(animationId);
        return this;
    }

    @Deprecated
    public BitmapRequestBuilder<ModelType, TranscodeType> animate(Animation animation) {
        super.animate(animation);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> animate(Animator animator) {
        super.animate(animator);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> placeholder(int resourceId) {
        super.placeholder(resourceId);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> placeholder(Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> fallback(Drawable drawable) {
        super.fallback(drawable);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> fallback(int resourceId) {
        super.fallback(resourceId);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> error(int resourceId) {
        super.error(resourceId);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> error(Drawable drawable) {
        super.error(drawable);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> listener(RequestListener<? super ModelType, TranscodeType> requestListener) {
        super.listener(requestListener);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> skipMemoryCache(boolean skip) {
        super.skipMemoryCache(skip);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> diskCacheStrategy(DiskCacheStrategy strategy) {
        super.diskCacheStrategy(strategy);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> override(int width, int height) {
        super.override(width, height);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(GenericRequestBuilder<?, ?, ?, TranscodeType> thumbnailRequest) {
        super.thumbnail((GenericRequestBuilder) thumbnailRequest);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> sourceEncoder(Encoder<ImageVideoWrapper> sourceEncoder) {
        super.sourceEncoder(sourceEncoder);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> dontTransform() {
        super.dontTransform();
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> signature(Key signature) {
        super.signature(signature);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> load(ModelType model) {
        super.load(model);
        return this;
    }

    public BitmapRequestBuilder<ModelType, TranscodeType> clone() {
        return (BitmapRequestBuilder) super.clone();
    }

    public Target<TranscodeType> into(ImageView view) {
        return super.into(view);
    }

    void applyFitCenter() {
        fitCenter();
    }

    void applyCenterCrop() {
        centerCrop();
    }
}
