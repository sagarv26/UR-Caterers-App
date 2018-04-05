package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;

public final class PreFillType {
    static final Config DEFAULT_CONFIG = Config.RGB_565;
    private final Config config;
    private final int height;
    private final int weight;
    private final int width;

    public static class Builder {
        private Config config;
        private final int height;
        private int weight;
        private final int width;

        public Builder(int size) {
            this(size, size);
        }

        public Builder(int width, int height) {
            this.weight = 1;
            if (width <= 0) {
                throw new IllegalArgumentException("Width must be > 0");
            } else if (height <= 0) {
                throw new IllegalArgumentException("Height must be > 0");
            } else {
                this.width = width;
                this.height = height;
            }
        }

        public Builder setConfig(Config config) {
            this.config = config;
            return this;
        }

        Config getConfig() {
            return this.config;
        }

        public Builder setWeight(int weight) {
            if (weight <= 0) {
                throw new IllegalArgumentException("Weight must be > 0");
            }
            this.weight = weight;
            return this;
        }

        PreFillType build() {
            return new PreFillType(this.width, this.height, this.config, this.weight);
        }
    }

    PreFillType(int width, int height, Config config, int weight) {
        if (config == null) {
            throw new NullPointerException("Config must not be null");
        }
        this.width = width;
        this.height = height;
        this.config = config;
        this.weight = weight;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    Config getConfig() {
        return this.config;
    }

    int getWeight() {
        return this.weight;
    }

    public boolean equals(Object o) {
        if (!(o instanceof PreFillType)) {
            return false;
        }
        PreFillType other = (PreFillType) o;
        if (this.height == other.height && this.width == other.width && this.weight == other.weight && this.config == other.config) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((this.width * 31) + this.height) * 31) + this.config.hashCode()) * 31) + this.weight;
    }

    public String toString() {
        return "PreFillSize{width=" + this.width + ", height=" + this.height + ", config=" + this.config + ", weight=" + this.weight + '}';
    }
}
