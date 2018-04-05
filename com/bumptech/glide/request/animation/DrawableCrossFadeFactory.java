package com.bumptech.glide.request.animation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class DrawableCrossFadeFactory<T extends Drawable> implements GlideAnimationFactory<T> {
    private static final int DEFAULT_DURATION_MS = 300;
    private final ViewAnimationFactory<T> animationFactory;
    private final int duration;
    private DrawableCrossFadeViewAnimation<T> firstResourceAnimation;
    private DrawableCrossFadeViewAnimation<T> secondResourceAnimation;

    private static class DefaultAnimationFactory implements AnimationFactory {
        private final int duration;

        DefaultAnimationFactory(int duration) {
            this.duration = duration;
        }

        public Animation build() {
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration((long) this.duration);
            return animation;
        }
    }

    public DrawableCrossFadeFactory() {
        this(DEFAULT_DURATION_MS);
    }

    public DrawableCrossFadeFactory(int duration) {
        this(new ViewAnimationFactory(new DefaultAnimationFactory(duration)), duration);
    }

    public DrawableCrossFadeFactory(Context context, int defaultAnimationId, int duration) {
        this(new ViewAnimationFactory(context, defaultAnimationId), duration);
    }

    public DrawableCrossFadeFactory(Animation defaultAnimation, int duration) {
        this(new ViewAnimationFactory(defaultAnimation), duration);
    }

    DrawableCrossFadeFactory(ViewAnimationFactory<T> animationFactory, int duration) {
        this.animationFactory = animationFactory;
        this.duration = duration;
    }

    public GlideAnimation<T> build(boolean isFromMemoryCache, boolean isFirstResource) {
        if (isFromMemoryCache) {
            return NoAnimation.get();
        }
        if (isFirstResource) {
            return getFirstResourceAnimation();
        }
        return getSecondResourceAnimation();
    }

    private GlideAnimation<T> getFirstResourceAnimation() {
        if (this.firstResourceAnimation == null) {
            this.firstResourceAnimation = new DrawableCrossFadeViewAnimation(this.animationFactory.build(false, true), this.duration);
        }
        return this.firstResourceAnimation;
    }

    private GlideAnimation<T> getSecondResourceAnimation() {
        if (this.secondResourceAnimation == null) {
            this.secondResourceAnimation = new DrawableCrossFadeViewAnimation(this.animationFactory.build(false, false), this.duration);
        }
        return this.secondResourceAnimation;
    }
}
