package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

class EngineKey implements Key {
    private static final String EMPTY_LOG_STRING = "";
    private final ResourceDecoder cacheDecoder;
    private final ResourceDecoder decoder;
    private final ResourceEncoder encoder;
    private int hashCode;
    private final int height;
    private final String id;
    private Key originalKey;
    private final Key signature;
    private final Encoder sourceEncoder;
    private String stringKey;
    private final ResourceTranscoder transcoder;
    private final Transformation transformation;
    private final int width;

    public EngineKey(String id, Key signature, int width, int height, ResourceDecoder cacheDecoder, ResourceDecoder decoder, Transformation transformation, ResourceEncoder encoder, ResourceTranscoder transcoder, Encoder sourceEncoder) {
        this.id = id;
        this.signature = signature;
        this.width = width;
        this.height = height;
        this.cacheDecoder = cacheDecoder;
        this.decoder = decoder;
        this.transformation = transformation;
        this.encoder = encoder;
        this.transcoder = transcoder;
        this.sourceEncoder = sourceEncoder;
    }

    public Key getOriginalKey() {
        if (this.originalKey == null) {
            this.originalKey = new OriginalKey(this.id, this.signature);
        }
        return this.originalKey;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngineKey engineKey = (EngineKey) o;
        if (!this.id.equals(engineKey.id) || !this.signature.equals(engineKey.signature) || this.height != engineKey.height || this.width != engineKey.width) {
            return false;
        }
        if (((this.transformation == null ? 1 : 0) ^ (engineKey.transformation == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.transformation != null && !this.transformation.getId().equals(engineKey.transformation.getId())) {
            return false;
        }
        int i;
        if (this.decoder == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (engineKey.decoder == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.decoder != null && !this.decoder.getId().equals(engineKey.decoder.getId())) {
            return false;
        }
        if (this.cacheDecoder == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (engineKey.cacheDecoder == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.cacheDecoder != null && !this.cacheDecoder.getId().equals(engineKey.cacheDecoder.getId())) {
            return false;
        }
        if (this.encoder == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (engineKey.encoder == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.encoder != null && !this.encoder.getId().equals(engineKey.encoder.getId())) {
            return false;
        }
        if (this.transcoder == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (engineKey.transcoder == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.transcoder != null && !this.transcoder.getId().equals(engineKey.transcoder.getId())) {
            return false;
        }
        if (this.sourceEncoder == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (engineKey.sourceEncoder == null ? 1 : 0)) != 0) {
            return false;
        }
        if (this.sourceEncoder == null || this.sourceEncoder.getId().equals(engineKey.sourceEncoder.getId())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        if (this.hashCode == 0) {
            int hashCode;
            this.hashCode = this.id.hashCode();
            this.hashCode = (this.hashCode * 31) + this.signature.hashCode();
            this.hashCode = (this.hashCode * 31) + this.width;
            this.hashCode = (this.hashCode * 31) + this.height;
            this.hashCode = (this.cacheDecoder != null ? this.cacheDecoder.getId().hashCode() : 0) + (this.hashCode * 31);
            int i2 = this.hashCode * 31;
            if (this.decoder != null) {
                hashCode = this.decoder.getId().hashCode();
            } else {
                hashCode = 0;
            }
            this.hashCode = hashCode + i2;
            i2 = this.hashCode * 31;
            if (this.transformation != null) {
                hashCode = this.transformation.getId().hashCode();
            } else {
                hashCode = 0;
            }
            this.hashCode = hashCode + i2;
            i2 = this.hashCode * 31;
            if (this.encoder != null) {
                hashCode = this.encoder.getId().hashCode();
            } else {
                hashCode = 0;
            }
            this.hashCode = hashCode + i2;
            i2 = this.hashCode * 31;
            if (this.transcoder != null) {
                hashCode = this.transcoder.getId().hashCode();
            } else {
                hashCode = 0;
            }
            this.hashCode = hashCode + i2;
            hashCode = this.hashCode * 31;
            if (this.sourceEncoder != null) {
                i = this.sourceEncoder.getId().hashCode();
            }
            this.hashCode = hashCode + i;
        }
        return this.hashCode;
    }

    public String toString() {
        if (this.stringKey == null) {
            String id;
            StringBuilder append = new StringBuilder().append("EngineKey{").append(this.id).append('+').append(this.signature).append("+[").append(this.width).append('x').append(this.height).append("]+").append('\'');
            if (this.cacheDecoder != null) {
                id = this.cacheDecoder.getId();
            } else {
                id = "";
            }
            append = append.append(id).append('\'').append('+').append('\'');
            if (this.decoder != null) {
                id = this.decoder.getId();
            } else {
                id = "";
            }
            append = append.append(id).append('\'').append('+').append('\'');
            if (this.transformation != null) {
                id = this.transformation.getId();
            } else {
                id = "";
            }
            append = append.append(id).append('\'').append('+').append('\'');
            if (this.encoder != null) {
                id = this.encoder.getId();
            } else {
                id = "";
            }
            append = append.append(id).append('\'').append('+').append('\'');
            if (this.transcoder != null) {
                id = this.transcoder.getId();
            } else {
                id = "";
            }
            append = append.append(id).append('\'').append('+').append('\'');
            if (this.sourceEncoder != null) {
                id = this.sourceEncoder.getId();
            } else {
                id = "";
            }
            this.stringKey = append.append(id).append('\'').append('}').toString();
        }
        return this.stringKey;
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        byte[] dimensions = ByteBuffer.allocate(8).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        messageDigest.update(this.id.getBytes(Key.STRING_CHARSET_NAME));
        messageDigest.update(dimensions);
        messageDigest.update((this.cacheDecoder != null ? this.cacheDecoder.getId() : "").getBytes(Key.STRING_CHARSET_NAME));
        messageDigest.update((this.decoder != null ? this.decoder.getId() : "").getBytes(Key.STRING_CHARSET_NAME));
        messageDigest.update((this.transformation != null ? this.transformation.getId() : "").getBytes(Key.STRING_CHARSET_NAME));
        messageDigest.update((this.encoder != null ? this.encoder.getId() : "").getBytes(Key.STRING_CHARSET_NAME));
        messageDigest.update((this.sourceEncoder != null ? this.sourceEncoder.getId() : "").getBytes(Key.STRING_CHARSET_NAME));
    }
}
