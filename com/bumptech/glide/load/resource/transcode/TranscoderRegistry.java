package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.util.MultiClassKey;
import java.util.HashMap;
import java.util.Map;

public class TranscoderRegistry {
    private static final MultiClassKey GET_KEY = new MultiClassKey();
    private final Map<MultiClassKey, ResourceTranscoder<?, ?>> factories = new HashMap();

    public <Z, R> void register(Class<Z> decodedClass, Class<R> transcodedClass, ResourceTranscoder<Z, R> transcoder) {
        this.factories.put(new MultiClassKey(decodedClass, transcodedClass), transcoder);
    }

    public <Z, R> ResourceTranscoder<Z, R> get(Class<Z> decodedClass, Class<R> transcodedClass) {
        if (decodedClass.equals(transcodedClass)) {
            return UnitTranscoder.get();
        }
        synchronized (GET_KEY) {
            GET_KEY.set(decodedClass, transcodedClass);
            ResourceTranscoder<Z, R> result = (ResourceTranscoder) this.factories.get(GET_KEY);
        }
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("No transcoder registered for " + decodedClass + " and " + transcodedClass);
    }
}
