package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.util.Util;
import java.util.TreeMap;

@TargetApi(19)
class SizeStrategy implements LruPoolStrategy {
    private static final int MAX_SIZE_MULTIPLE = 8;
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();
    private final TreeMap<Integer, Integer> sortedSizes = new PrettyPrintTreeMap();

    static final class Key implements Poolable {
        private final KeyPool pool;
        private int size;

        Key(KeyPool pool) {
            this.pool = pool;
        }

        public void init(int size) {
            this.size = size;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Key)) {
                return false;
            }
            if (this.size == ((Key) o).size) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.size;
        }

        public String toString() {
            return SizeStrategy.getBitmapString(this.size);
        }

        public void offer() {
            this.pool.offer(this);
        }
    }

    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        public Key get(int size) {
            Key result = (Key) get();
            result.init(size);
            return result;
        }

        protected Key create() {
            return new Key(this);
        }
    }

    SizeStrategy() {
    }

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap));
        this.groupedMap.put(key, bitmap);
        Integer current = (Integer) this.sortedSizes.get(Integer.valueOf(key.size));
        this.sortedSizes.put(Integer.valueOf(key.size), Integer.valueOf(current == null ? 1 : current.intValue() + 1));
    }

    public Bitmap get(int width, int height, Config config) {
        int size = Util.getBitmapByteSize(width, height, config);
        Key key = this.keyPool.get(size);
        Integer possibleSize = (Integer) this.sortedSizes.ceilingKey(Integer.valueOf(size));
        if (!(possibleSize == null || possibleSize.intValue() == size || possibleSize.intValue() > size * 8)) {
            this.keyPool.offer(key);
            key = this.keyPool.get(possibleSize.intValue());
        }
        Bitmap result = (Bitmap) this.groupedMap.get(key);
        if (result != null) {
            result.reconfigure(width, height, config);
            decrementBitmapOfSize(possibleSize);
        }
        return result;
    }

    public Bitmap removeLast() {
        Bitmap removed = (Bitmap) this.groupedMap.removeLast();
        if (removed != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(removed)));
        }
        return removed;
    }

    private void decrementBitmapOfSize(Integer size) {
        Integer current = (Integer) this.sortedSizes.get(size);
        if (current.intValue() == 1) {
            this.sortedSizes.remove(size);
        } else {
            this.sortedSizes.put(size, Integer.valueOf(current.intValue() - 1));
        }
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(bitmap);
    }

    public String logBitmap(int width, int height, Config config) {
        return getBitmapString(Util.getBitmapByteSize(width, height, config));
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String toString() {
        return "SizeStrategy:\n  " + this.groupedMap + "\n" + "  SortedSizes" + this.sortedSizes;
    }

    private static String getBitmapString(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap));
    }

    private static String getBitmapString(int size) {
        return "[" + size + "]";
    }
}
