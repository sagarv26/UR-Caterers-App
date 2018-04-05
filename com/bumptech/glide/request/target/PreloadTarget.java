package com.bumptech.glide.request.target;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;

public final class PreloadTarget<Z> extends SimpleTarget<Z> {
    public static <Z> PreloadTarget<Z> obtain(int width, int height) {
        return new PreloadTarget(width, height);
    }

    private PreloadTarget(int width, int height) {
        super(width, height);
    }

    public void onResourceReady(Z z, GlideAnimation<? super Z> glideAnimation) {
        Glide.clear((Target) this);
    }
}
