package com.bumptech.glide.util;

import android.view.View;
import com.bumptech.glide.ListPreloader.PreloadSizeProvider;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Arrays;

public class ViewPreloadSizeProvider<T> implements PreloadSizeProvider<T>, SizeReadyCallback {
    private int[] size;
    private SizeViewTarget viewTarget;

    private static final class SizeViewTarget extends ViewTarget<View, Object> {
        public SizeViewTarget(View view, SizeReadyCallback callback) {
            super(view);
            getSize(callback);
        }

        public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
        }
    }

    public ViewPreloadSizeProvider(View view) {
        setView(view);
    }

    public int[] getPreloadSize(T t, int adapterPosition, int itemPosition) {
        if (this.size == null) {
            return null;
        }
        return Arrays.copyOf(this.size, this.size.length);
    }

    public void onSizeReady(int width, int height) {
        this.size = new int[]{width, height};
        this.viewTarget = null;
    }

    public void setView(View view) {
        if (this.size == null && this.viewTarget == null) {
            this.viewTarget = new SizeViewTarget(view, this);
        }
    }
}
