package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy {
    private static final Config[] ALPHA_8_IN_CONFIGS = new Config[]{Config.ALPHA_8};
    private static final Config[] ARGB_4444_IN_CONFIGS = new Config[]{Config.ARGB_4444};
    private static final Config[] ARGB_8888_IN_CONFIGS = new Config[]{Config.ARGB_8888, null};
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Config[] RGB_565_IN_CONFIGS = new Config[]{Config.RGB_565};
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();
    private final Map<Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    static /* synthetic */ class C02451 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    static final class Key implements Poolable {
        private Config config;
        private final KeyPool pool;
        private int size;

        public Key(KeyPool pool) {
            this.pool = pool;
        }

        Key(KeyPool pool, int size, Config config) {
            this(pool);
            init(size, config);
        }

        public void init(int size, Config config) {
            this.size = size;
            this.config = config;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }

        public boolean equals(Object o) {
            if (!(o instanceof Key)) {
                return false;
            }
            Key other = (Key) o;
            if (this.size != other.size) {
                return false;
            }
            if (this.config == null) {
                if (other.config != null) {
                    return false;
                }
            } else if (!this.config.equals(other.config)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.size * 31) + (this.config != null ? this.config.hashCode() : 0);
        }
    }

    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        public Key get(int size, Config config) {
            Key result = (Key) get();
            result.init(size, config);
            return result;
        }

        protected Key create() {
            return new Key(this);
        }
    }

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(key, bitmap);
        NavigableMap<Integer, Integer> sizes = getSizesForConfig(bitmap.getConfig());
        Integer current = (Integer) sizes.get(Integer.valueOf(key.size));
        sizes.put(Integer.valueOf(key.size), Integer.valueOf(current == null ? 1 : current.intValue() + 1));
    }

    public Bitmap get(int width, int height, Config config) {
        int size = Util.getBitmapByteSize(width, height, config);
        Bitmap result = (Bitmap) this.groupedMap.get(findBestKey(this.keyPool.get(size, config), size, config));
        if (result != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(result)), result.getConfig());
            result.reconfigure(width, height, result.getConfig() != null ? result.getConfig() : Config.ARGB_8888);
        }
        return result;
    }

    private Key findBestKey(Key key, int size, Config config) {
        Key result = key;
        Config[] arr$ = getInConfigs(config);
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Config possibleConfig = arr$[i$];
            Integer possibleSize = (Integer) getSizesForConfig(possibleConfig).ceilingKey(Integer.valueOf(size));
            if (possibleSize == null || possibleSize.intValue() > size * 8) {
                i$++;
            } else {
                if (possibleSize.intValue() == size) {
                    if (possibleConfig == null) {
                        if (config == null) {
                            return result;
                        }
                    } else if (possibleConfig.equals(config)) {
                        return result;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(possibleSize.intValue(), possibleConfig);
            }
        }
        return result;
    }

    public Bitmap removeLast() {
        Bitmap removed = (Bitmap) this.groupedMap.removeLast();
        if (removed != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(removed)), removed.getConfig());
        }
        return removed;
    }

    private void decrementBitmapOfSize(Integer size, Config config) {
        NavigableMap<Integer, Integer> sizes = getSizesForConfig(config);
        Integer current = (Integer) sizes.get(size);
        if (current.intValue() == 1) {
            sizes.remove(size);
        } else {
            sizes.put(size, Integer.valueOf(current.intValue() - 1));
        }
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Config config) {
        NavigableMap<Integer, Integer> sizes = (NavigableMap) this.sortedSizes.get(config);
        if (sizes != null) {
            return sizes;
        }
        sizes = new TreeMap();
        this.sortedSizes.put(config, sizes);
        return sizes;
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }

    public String logBitmap(int width, int height, Config config) {
        return getBitmapString(Util.getBitmapByteSize(width, height, config), config);
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("SizeConfigStrategy{groupedMap=").append(this.groupedMap).append(", sortedSizes=(");
        for (Entry<Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            sb.append(entry.getKey()).append('[').append(entry.getValue()).append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        return sb.append(")}").toString();
    }

    private static String getBitmapString(int size, Config config) {
        return "[" + size + "](" + config + ")";
    }

    private static Config[] getInConfigs(Config requested) {
        switch (C02451.$SwitchMap$android$graphics$Bitmap$Config[requested.ordinal()]) {
            case 1:
                return ARGB_8888_IN_CONFIGS;
            case 2:
                return RGB_565_IN_CONFIGS;
            case 3:
                return ARGB_4444_IN_CONFIGS;
            case 4:
                return ALPHA_8_IN_CONFIGS;
            default:
                return new Config[]{requested};
        }
    }
}
