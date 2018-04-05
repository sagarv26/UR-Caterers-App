package com.bumptech.glide.provider;

import com.bumptech.glide.util.MultiClassKey;
import java.util.HashMap;
import java.util.Map;

public class DataLoadProviderRegistry {
    private static final MultiClassKey GET_KEY = new MultiClassKey();
    private final Map<MultiClassKey, DataLoadProvider<?, ?>> providers = new HashMap();

    public <T, Z> void register(Class<T> dataClass, Class<Z> resourceClass, DataLoadProvider<T, Z> provider) {
        this.providers.put(new MultiClassKey(dataClass, resourceClass), provider);
    }

    public <T, Z> DataLoadProvider<T, Z> get(Class<T> dataClass, Class<Z> resourceClass) {
        synchronized (GET_KEY) {
            GET_KEY.set(dataClass, resourceClass);
            DataLoadProvider<?, ?> result = (DataLoadProvider) this.providers.get(GET_KEY);
        }
        if (result == null) {
            return EmptyDataLoadProvider.get();
        }
        return result;
    }
}
