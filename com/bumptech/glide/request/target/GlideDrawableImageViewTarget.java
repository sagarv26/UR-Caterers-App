package com.bumptech.glide.request.target;

import android.widget.ImageView;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;

public class GlideDrawableImageViewTarget extends ImageViewTarget<GlideDrawable> {
    private static final float SQUARE_RATIO_MARGIN = 0.05f;
    private int maxLoopCount;
    private GlideDrawable resource;

    public GlideDrawableImageViewTarget(ImageView view) {
        this(view, -1);
    }

    public GlideDrawableImageViewTarget(ImageView view, int maxLoopCount) {
        super(view);
        this.maxLoopCount = maxLoopCount;
    }

    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
        if (!resource.isAnimated()) {
            float drawableRatio = ((float) resource.getIntrinsicWidth()) / ((float) resource.getIntrinsicHeight());
            if (Math.abs((((float) ((ImageView) this.view).getWidth()) / ((float) ((ImageView) this.view).getHeight())) - 1.0f) <= SQUARE_RATIO_MARGIN && Math.abs(drawableRatio - 1.0f) <= SQUARE_RATIO_MARGIN) {
                resource = new SquaringDrawable(resource, ((ImageView) this.view).getWidth());
            }
        }
        super.onResourceReady(resource, animation);
        this.resource = resource;
        resource.setLoopCount(this.maxLoopCount);
        resource.start();
    }

    protected void setResource(GlideDrawable resource) {
        ((ImageView) this.view).setImageDrawable(resource);
    }

    public void onStart() {
        if (this.resource != null) {
            this.resource.start();
        }
    }

    public void onStop() {
        if (this.resource != null) {
            this.resource.stop();
        }
    }
}
