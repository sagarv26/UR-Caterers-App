package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.UriLoader;
import java.io.InputStream;

public class StreamUriLoader extends UriLoader<InputStream> implements StreamModelLoader<Uri> {

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
        public ModelLoader<Uri, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new StreamUriLoader(context, factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public StreamUriLoader(Context context) {
        this(context, Glide.buildStreamModelLoader(GlideUrl.class, context));
    }

    public StreamUriLoader(Context context, ModelLoader<GlideUrl, InputStream> urlLoader) {
        super(context, urlLoader);
    }

    protected DataFetcher<InputStream> getLocalUriFetcher(Context context, Uri uri) {
        return new StreamLocalUriFetcher(context, uri);
    }

    protected DataFetcher<InputStream> getAssetPathFetcher(Context context, String assetPath) {
        return new StreamAssetPathFetcher(context.getApplicationContext().getAssets(), assetPath);
    }
}
