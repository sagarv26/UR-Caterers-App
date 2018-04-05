package com.bumptech.glide.load.resource.gif;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.view.Gravity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifFrameLoader.FrameCallback;

public class GifDrawable extends GlideDrawable implements FrameCallback {
    private boolean applyGravity;
    private final GifDecoder decoder;
    private final Rect destRect;
    private final GifFrameLoader frameLoader;
    private boolean isRecycled;
    private boolean isRunning;
    private boolean isStarted;
    private boolean isVisible;
    private int loopCount;
    private int maxLoopCount;
    private final Paint paint;
    private final GifState state;

    static class GifState extends ConstantState {
        private static final int GRAVITY = 119;
        BitmapPool bitmapPool;
        BitmapProvider bitmapProvider;
        Context context;
        byte[] data;
        Bitmap firstFrame;
        Transformation<Bitmap> frameTransformation;
        GifHeader gifHeader;
        int targetHeight;
        int targetWidth;

        public GifState(GifHeader header, byte[] data, Context context, Transformation<Bitmap> frameTransformation, int targetWidth, int targetHeight, BitmapProvider provider, BitmapPool bitmapPool, Bitmap firstFrame) {
            if (firstFrame == null) {
                throw new NullPointerException("The first frame of the GIF must not be null");
            }
            this.gifHeader = header;
            this.data = data;
            this.bitmapPool = bitmapPool;
            this.firstFrame = firstFrame;
            this.context = context.getApplicationContext();
            this.frameTransformation = frameTransformation;
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            this.bitmapProvider = provider;
        }

        public GifState(GifState original) {
            if (original != null) {
                this.gifHeader = original.gifHeader;
                this.data = original.data;
                this.context = original.context;
                this.frameTransformation = original.frameTransformation;
                this.targetWidth = original.targetWidth;
                this.targetHeight = original.targetHeight;
                this.bitmapProvider = original.bitmapProvider;
                this.bitmapPool = original.bitmapPool;
                this.firstFrame = original.firstFrame;
            }
        }

        public Drawable newDrawable(Resources res) {
            return newDrawable();
        }

        public Drawable newDrawable() {
            return new GifDrawable(this);
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }

    public GifDrawable(Context context, BitmapProvider bitmapProvider, BitmapPool bitmapPool, Transformation<Bitmap> frameTransformation, int targetFrameWidth, int targetFrameHeight, GifHeader gifHeader, byte[] data, Bitmap firstFrame) {
        this(new GifState(gifHeader, data, context, frameTransformation, targetFrameWidth, targetFrameHeight, bitmapProvider, bitmapPool, firstFrame));
    }

    public GifDrawable(GifDrawable other, Bitmap firstFrame, Transformation<Bitmap> frameTransformation) {
        this(new GifState(other.state.gifHeader, other.state.data, other.state.context, frameTransformation, other.state.targetWidth, other.state.targetHeight, other.state.bitmapProvider, other.state.bitmapPool, firstFrame));
    }

    GifDrawable(GifState state) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        if (state == null) {
            throw new NullPointerException("GifState must not be null");
        }
        this.state = state;
        this.decoder = new GifDecoder(state.bitmapProvider);
        this.paint = new Paint();
        this.decoder.setData(state.gifHeader, state.data);
        this.frameLoader = new GifFrameLoader(state.context, this, this.decoder, state.targetWidth, state.targetHeight);
        this.frameLoader.setFrameTransformation(state.frameTransformation);
    }

    GifDrawable(GifDecoder decoder, GifFrameLoader frameLoader, Bitmap firstFrame, BitmapPool bitmapPool, Paint paint) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        this.decoder = decoder;
        this.frameLoader = frameLoader;
        this.state = new GifState(null);
        this.paint = paint;
        this.state.bitmapPool = bitmapPool;
        this.state.firstFrame = firstFrame;
    }

    public Bitmap getFirstFrame() {
        return this.state.firstFrame;
    }

    public void setFrameTransformation(Transformation<Bitmap> frameTransformation, Bitmap firstFrame) {
        if (firstFrame == null) {
            throw new NullPointerException("The first frame of the GIF must not be null");
        } else if (frameTransformation == null) {
            throw new NullPointerException("The frame transformation must not be null");
        } else {
            this.state.frameTransformation = frameTransformation;
            this.state.firstFrame = firstFrame;
            this.frameLoader.setFrameTransformation(frameTransformation);
        }
    }

    public GifDecoder getDecoder() {
        return this.decoder;
    }

    public Transformation<Bitmap> getFrameTransformation() {
        return this.state.frameTransformation;
    }

    public byte[] getData() {
        return this.state.data;
    }

    public int getFrameCount() {
        return this.decoder.getFrameCount();
    }

    private void resetLoopCount() {
        this.loopCount = 0;
    }

    public void start() {
        this.isStarted = true;
        resetLoopCount();
        if (this.isVisible) {
            startRunning();
        }
    }

    public void stop() {
        this.isStarted = false;
        stopRunning();
        if (VERSION.SDK_INT < 11) {
            reset();
        }
    }

    private void reset() {
        this.frameLoader.clear();
        invalidateSelf();
    }

    private void startRunning() {
        if (this.decoder.getFrameCount() == 1) {
            invalidateSelf();
        } else if (!this.isRunning) {
            this.isRunning = true;
            this.frameLoader.start();
            invalidateSelf();
        }
    }

    private void stopRunning() {
        this.isRunning = false;
        this.frameLoader.stop();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        this.isVisible = visible;
        if (!visible) {
            stopRunning();
        } else if (this.isStarted) {
            startRunning();
        }
        return super.setVisible(visible, restart);
    }

    public int getIntrinsicWidth() {
        return this.state.firstFrame.getWidth();
    }

    public int getIntrinsicHeight() {
        return this.state.firstFrame.getHeight();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.applyGravity = true;
    }

    public void draw(Canvas canvas) {
        if (!this.isRecycled) {
            if (this.applyGravity) {
                Gravity.apply(119, getIntrinsicWidth(), getIntrinsicHeight(), getBounds(), this.destRect);
                this.applyGravity = false;
            }
            Bitmap currentFrame = this.frameLoader.getCurrentFrame();
            canvas.drawBitmap(currentFrame != null ? currentFrame : this.state.firstFrame, null, this.destRect, this.paint);
        }
    }

    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        return -2;
    }

    @TargetApi(11)
    public void onFrameReady(int frameIndex) {
        if (VERSION.SDK_INT < 11 || getCallback() != null) {
            invalidateSelf();
            if (frameIndex == this.decoder.getFrameCount() - 1) {
                this.loopCount++;
            }
            if (this.maxLoopCount != -1 && this.loopCount >= this.maxLoopCount) {
                stop();
                return;
            }
            return;
        }
        stop();
        reset();
    }

    public ConstantState getConstantState() {
        return this.state;
    }

    public void recycle() {
        this.isRecycled = true;
        this.state.bitmapPool.put(this.state.firstFrame);
        this.frameLoader.clear();
        this.frameLoader.stop();
    }

    boolean isRecycled() {
        return this.isRecycled;
    }

    public boolean isAnimated() {
        return true;
    }

    public void setLoopCount(int loopCount) {
        if (loopCount <= 0 && loopCount != -1 && loopCount != 0) {
            throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
        } else if (loopCount == 0) {
            this.maxLoopCount = this.decoder.getLoopCount();
        } else {
            this.maxLoopCount = loopCount;
        }
    }
}
