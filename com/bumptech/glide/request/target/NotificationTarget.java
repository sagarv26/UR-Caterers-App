package com.bumptech.glide.request.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;

public class NotificationTarget extends SimpleTarget<Bitmap> {
    private final Context context;
    private final Notification notification;
    private final int notificationId;
    private final RemoteViews remoteViews;
    private final int viewId;

    public NotificationTarget(Context context, RemoteViews remoteViews, int viewId, Notification notification, int notificationId) {
        this(context, remoteViews, viewId, Integer.MIN_VALUE, Integer.MIN_VALUE, notification, notificationId);
    }

    public NotificationTarget(Context context, RemoteViews remoteViews, int viewId, int width, int height, Notification notification, int notificationId) {
        super(width, height);
        if (context == null) {
            throw new NullPointerException("Context must not be null!");
        } else if (notification == null) {
            throw new NullPointerException("Notification object can not be null!");
        } else if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context;
            this.viewId = viewId;
            this.notification = notification;
            this.notificationId = notificationId;
            this.remoteViews = remoteViews;
        }
    }

    private void update() {
        ((NotificationManager) this.context.getSystemService("notification")).notify(this.notificationId, this.notification);
    }

    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, resource);
        update();
    }
}
