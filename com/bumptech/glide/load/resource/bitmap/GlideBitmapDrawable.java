package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.view.Gravity;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class GlideBitmapDrawable extends GlideDrawable {
    private boolean applyGravity;
    private final Rect destRect;
    private int height;
    private boolean mutated;
    private BitmapState state;
    private int width;

    static class BitmapState extends ConstantState {
        private static final Paint DEFAULT_PAINT = new Paint(6);
        private static final int DEFAULT_PAINT_FLAGS = 6;
        private static final int GRAVITY = 119;
        final Bitmap bitmap;
        Paint paint;
        int targetDensity;

        public BitmapState(Bitmap bitmap) {
            this.paint = DEFAULT_PAINT;
            this.bitmap = bitmap;
        }

        BitmapState(BitmapState other) {
            this(other.bitmap);
            this.targetDensity = other.targetDensity;
        }

        void setColorFilter(ColorFilter colorFilter) {
            mutatePaint();
            this.paint.setColorFilter(colorFilter);
        }

        void setAlpha(int alpha) {
            mutatePaint();
            this.paint.setAlpha(alpha);
        }

        void mutatePaint() {
            if (DEFAULT_PAINT == this.paint) {
                this.paint = new Paint(6);
            }
        }

        public Drawable newDrawable() {
            return new GlideBitmapDrawable(null, this);
        }

        public Drawable newDrawable(Resources res) {
            return new GlideBitmapDrawable(res, this);
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }

    public GlideBitmapDrawable(Resources res, Bitmap bitmap) {
        this(res, new BitmapState(bitmap));
    }

    GlideBitmapDrawable(Resources res, BitmapState state) {
        this.destRect = new Rect();
        if (state == null) {
            throw new NullPointerException("BitmapState must not be null");
        }
        int targetDensity;
        this.state = state;
        if (res != null) {
            int density = res.getDisplayMetrics().densityDpi;
            if (density == 0) {
                targetDensity = 160;
            } else {
                targetDensity = density;
            }
            state.targetDensity = targetDensity;
        } else {
            targetDensity = state.targetDensity;
        }
        this.width = state.bitmap.getScaledWidth(targetDensity);
        this.height = state.bitmap.getScaledHeight(targetDensity);
    }

    public int getIntrinsicWidth() {
        return this.width;
    }

    public int getIntrinsicHeight() {
        return this.height;
    }

    public boolean isAnimated() {
        return false;
    }

    public void setLoopCount(int loopCount) {
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isRunning() {
        return false;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.applyGravity = true;
    }

    public ConstantState getConstantState() {
        return this.state;
    }

    public void draw(Canvas canvas) {
        if (this.applyGravity) {
            Gravity.apply(119, this.width, this.height, getBounds(), this.destRect);
            this.applyGravity = false;
        }
        canvas.drawBitmap(this.state.bitmap, null, this.destRect, this.state.paint);
    }

    public void setAlpha(int alpha) {
        if (this.state.paint.getAlpha() != alpha) {
            this.state.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.state.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getOpacity() {
        Bitmap bm = this.state.bitmap;
        return (bm == null || bm.hasAlpha() || this.state.paint.getAlpha() < 255) ? -3 : -1;
    }

    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.state = new BitmapState(this.state);
            this.mutated = true;
        }
        return this;
    }

    public Bitmap getBitmap() {
        return this.state.bitmap;
    }
}
