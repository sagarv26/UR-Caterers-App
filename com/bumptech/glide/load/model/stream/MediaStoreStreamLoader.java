package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.MediaStoreThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;

public class MediaStoreStreamLoader implements ModelLoader<Uri, InputStream> {
    private final Context context;
    private final ModelLoader<Uri, InputStream> uriLoader;

    public MediaStoreStreamLoader(Context context, ModelLoader<Uri, InputStream> uriLoader) {
        this.context = context;
        this.uriLoader = uriLoader;
    }

    public DataFetcher<InputStream> getResourceFetcher(Uri model, int width, int height) {
        return new MediaStoreThumbFetcher(this.context, model, this.uriLoader.getResourceFetcher(model, width, height), width, height);
    }
}
