package com.bumptech.glide.request.animation;

import android.view.View;
import android.view.animation.Animation;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

public class ViewAnimation<R> implements GlideAnimation<R> {
    private final AnimationFactory animationFactory;

    interface AnimationFactory {
        Animation build();
    }

    ViewAnimation(AnimationFactory animationFactory) {
        this.animationFactory = animationFactory;
    }

    public boolean animate(R r, ViewAdapter adapter) {
        View view = adapter.getView();
        if (view != null) {
            view.clearAnimation();
            view.startAnimation(this.animationFactory.build());
        }
        return false;
    }
}
