package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class GlideUrl {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private final Headers headers;
    private String safeStringUrl;
    private URL safeUrl;
    private final String stringUrl;
    private final URL url;

    public GlideUrl(URL url) {
        this(url, Headers.DEFAULT);
    }

    public GlideUrl(String url) {
        this(url, Headers.DEFAULT);
    }

    public GlideUrl(URL url, Headers headers) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null!");
        } else if (headers == null) {
            throw new IllegalArgumentException("Headers must not be null");
        } else {
            this.url = url;
            this.stringUrl = null;
            this.headers = headers;
        }
    }

    public GlideUrl(String url, Headers headers) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("String url must not be empty or null: " + url);
        } else if (headers == null) {
            throw new IllegalArgumentException("Headers must not be null");
        } else {
            this.stringUrl = url;
            this.url = null;
            this.headers = headers;
        }
    }

    public URL toURL() throws MalformedURLException {
        return getSafeUrl();
    }

    private URL getSafeUrl() throws MalformedURLException {
        if (this.safeUrl == null) {
            this.safeUrl = new URL(getSafeStringUrl());
        }
        return this.safeUrl;
    }

    public String toStringUrl() {
        return getSafeStringUrl();
    }

    private String getSafeStringUrl() {
        if (TextUtils.isEmpty(this.safeStringUrl)) {
            String unsafeStringUrl = this.stringUrl;
            if (TextUtils.isEmpty(unsafeStringUrl)) {
                unsafeStringUrl = this.url.toString();
            }
            this.safeStringUrl = Uri.encode(unsafeStringUrl, ALLOWED_URI_CHARS);
        }
        return this.safeStringUrl;
    }

    public Map<String, String> getHeaders() {
        return this.headers.getHeaders();
    }

    public String getCacheKey() {
        return this.stringUrl != null ? this.stringUrl : this.url.toString();
    }

    public String toString() {
        return getCacheKey() + '\n' + this.headers.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof GlideUrl)) {
            return false;
        }
        GlideUrl other = (GlideUrl) o;
        if (getCacheKey().equals(other.getCacheKey()) && this.headers.equals(other.headers)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (getCacheKey().hashCode() * 31) + this.headers.hashCode();
    }
}
