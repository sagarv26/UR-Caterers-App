package com.bumptech.glide.util;

public class MultiClassKey {
    private Class<?> first;
    private Class<?> second;

    public MultiClassKey(Class<?> first, Class<?> second) {
        set(first, second);
    }

    public void set(Class<?> first, Class<?> second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "MultiClassKey{first=" + this.first + ", second=" + this.second + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiClassKey that = (MultiClassKey) o;
        if (!this.first.equals(that.first)) {
            return false;
        }
        if (this.second.equals(that.second)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.first.hashCode() * 31) + this.second.hashCode();
    }
}
