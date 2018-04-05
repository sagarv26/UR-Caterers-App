package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements ViewAdapter {
    protected abstract void setResource(Z z);

    public ImageViewTarget(ImageView view) {
        super(view);
    }

    public Drawable getCurrentDrawable() {
        return ((ImageView) this.view).getDrawable();
    }

    public void setDrawable(Drawable drawable) {
        ((ImageView) this.view).setImageDrawable(drawable);
    }

    public void onLoadStarted(Drawable placeholder) {
        ((ImageView) this.view).setImageDrawable(placeholder);
    }

    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        ((ImageView) this.view).setImageDrawable(errorDrawable);
    }

    public void onLoadCleared(Drawable placeholder) {
        ((ImageView) this.view).setImageDrawable(placeholder);
    }

    public void onResourceReady(Z resource, GlideAnimation<? super Z> glideAnimation) {
        if (glideAnimation == null || !glideAnimation.animate(resource, this)) {
            setResource(resource);
        }
    }
}
