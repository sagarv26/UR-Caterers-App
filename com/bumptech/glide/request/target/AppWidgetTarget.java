package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;

public class AppWidgetTarget extends SimpleTarget<Bitmap> {
    private final ComponentName componentName;
    private final Context context;
    private final RemoteViews remoteViews;
    private final int viewId;
    private final int[] widgetIds;

    public AppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, int width, int height, int... widgetIds) {
        super(width, height);
        if (context == null) {
            throw new NullPointerException("Context can not be null!");
        } else if (widgetIds == null) {
            throw new NullPointerException("WidgetIds can not be null!");
        } else if (widgetIds.length == 0) {
            throw new IllegalArgumentException("WidgetIds must have length > 0");
        } else if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context;
            this.remoteViews = remoteViews;
            this.viewId = viewId;
            this.widgetIds = widgetIds;
            this.componentName = null;
        }
    }

    public AppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, int... widgetIds) {
        this(context, remoteViews, viewId, Integer.MIN_VALUE, Integer.MIN_VALUE, widgetIds);
    }

    public AppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, int width, int height, ComponentName componentName) {
        super(width, height);
        if (context == null) {
            throw new NullPointerException("Context can not be null!");
        } else if (componentName == null) {
            throw new NullPointerException("ComponentName can not be null!");
        } else if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context;
            this.remoteViews = remoteViews;
            this.viewId = viewId;
            this.componentName = componentName;
            this.widgetIds = null;
        }
    }

    public AppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, ComponentName componentName) {
        this(context, remoteViews, viewId, Integer.MIN_VALUE, Integer.MIN_VALUE, componentName);
    }

    private void update() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.context);
        if (this.componentName != null) {
            appWidgetManager.updateAppWidget(this.componentName, this.remoteViews);
        } else {
            appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews);
        }
    }

    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, resource);
        update();
    }
}
