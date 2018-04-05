package com.bumptech.glide.request.animation;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

public class DrawableCrossFadeViewAnimation<T extends Drawable> implements GlideAnimation<T> {
    private final GlideAnimation<T> defaultAnimation;
    private final int duration;

    public DrawableCrossFadeViewAnimation(GlideAnimation<T> defaultAnimation, int duration) {
        this.defaultAnimation = defaultAnimation;
        this.duration = duration;
    }

    public boolean animate(T current, ViewAdapter adapter) {
        if (adapter.getCurrentDrawable() != null) {
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{adapter.getCurrentDrawable(), current});
            transitionDrawable.setCrossFadeEnabled(true);
            transitionDrawable.startTransition(this.duration);
            adapter.setDrawable(transitionDrawable);
            return true;
        }
        this.defaultAnimation.animate(current, adapter);
        return false;
    }
}
