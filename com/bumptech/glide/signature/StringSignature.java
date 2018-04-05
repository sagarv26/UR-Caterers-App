package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class StringSignature implements Key {
    private final String signature;

    public StringSignature(String signature) {
        if (signature == null) {
            throw new NullPointerException("Signature cannot be null!");
        }
        this.signature = signature;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.signature.equals(((StringSignature) o).signature);
    }

    public int hashCode() {
        return this.signature.hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(this.signature.getBytes(Key.STRING_CHARSET_NAME));
    }

    public String toString() {
        return "StringSignature{signature='" + this.signature + '\'' + '}';
    }
}
