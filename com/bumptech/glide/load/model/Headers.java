package com.bumptech.glide.load.model;

import com.bumptech.glide.load.model.LazyHeaders.Builder;
import java.util.Collections;
import java.util.Map;

public interface Headers {
    public static final Headers DEFAULT = new Builder().build();
    @Deprecated
    public static final Headers NONE = new C04341();

    static class C04341 implements Headers {
        C04341() {
        }

        public Map<String, String> getHeaders() {
            return Collections.emptyMap();
        }
    }

    Map<String, String> getHeaders();
}
