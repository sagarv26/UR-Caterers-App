package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;

public abstract class BaseGlideUrlLoader<T> implements StreamModelLoader<T> {
    private final ModelLoader<GlideUrl, InputStream> concreteLoader;
    private final ModelCache<T, GlideUrl> modelCache;

    protected abstract String getUrl(T t, int i, int i2);

    public BaseGlideUrlLoader(Context context) {
        this(context, null);
    }

    public BaseGlideUrlLoader(Context context, ModelCache<T, GlideUrl> modelCache) {
        this(Glide.buildModelLoader(GlideUrl.class, InputStream.class, context), (ModelCache) modelCache);
    }

    public BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader) {
        this((ModelLoader) concreteLoader, null);
    }

    public BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader, ModelCache<T, GlideUrl> modelCache) {
        this.concreteLoader = concreteLoader;
        this.modelCache = modelCache;
    }

    public DataFetcher<InputStream> getResourceFetcher(T model, int width, int height) {
        GlideUrl glideUrl = null;
        if (this.modelCache != null) {
            glideUrl = (GlideUrl) this.modelCache.get(model, width, height);
        }
        if (glideUrl == null) {
            String stringURL = getUrl(model, width, height);
            if (TextUtils.isEmpty(stringURL)) {
                return null;
            }
            glideUrl = new GlideUrl(stringURL, getHeaders(model, width, height));
            if (this.modelCache != null) {
                this.modelCache.put(model, width, height, glideUrl);
            }
        }
        return this.concreteLoader.getResourceFetcher(glideUrl, width, height);
    }

    protected Headers getHeaders(T t, int width, int height) {
        return Headers.DEFAULT;
    }
}
