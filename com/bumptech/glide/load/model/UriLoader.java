package com.bumptech.glide.load.model;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.data.DataFetcher;

public abstract class UriLoader<T> implements ModelLoader<Uri, T> {
    private final Context context;
    private final ModelLoader<GlideUrl, T> urlLoader;

    protected abstract DataFetcher<T> getAssetPathFetcher(Context context, String str);

    protected abstract DataFetcher<T> getLocalUriFetcher(Context context, Uri uri);

    public UriLoader(Context context, ModelLoader<GlideUrl, T> urlLoader) {
        this.context = context;
        this.urlLoader = urlLoader;
    }

    public final DataFetcher<T> getResourceFetcher(Uri model, int width, int height) {
        String scheme = model.getScheme();
        if (isLocalUri(scheme)) {
            if (!AssetUriParser.isAssetUri(model)) {
                return getLocalUriFetcher(this.context, model);
            }
            return getAssetPathFetcher(this.context, AssetUriParser.toAssetPath(model));
        } else if (this.urlLoader == null) {
            return null;
        } else {
            if ("http".equals(scheme) || "https".equals(scheme)) {
                return this.urlLoader.getResourceFetcher(new GlideUrl(model.toString()), width, height);
            }
            return null;
        }
    }

    private static boolean isLocalUri(String scheme) {
        return "file".equals(scheme) || "content".equals(scheme) || "android.resource".equals(scheme);
    }
}
